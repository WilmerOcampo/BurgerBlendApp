package com.wo.burgerblend.service

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wo.burgerblend.domain.Order
import com.wo.burgerblend.domain.OrderItem
import com.wo.burgerblend.utils.Utils


class OrderService {
    private val reference = FirebaseDatabase.getInstance().getReference("orders")
    private val itemReference = FirebaseDatabase.getInstance().getReference("orderItems")
    private val foodService = FoodService()

    fun orders(userId: Long, callback: (List<Order>) -> Unit) {
        if (Utils.isNetworkAvailable()) {
            ordersFirebase(userId, callback)
        } else {
            // Manejo para obtener las órdenes desde SQLite u otro almacenamiento local
            // usersSQLite(callback)
        }
    }

    private fun ordersFirebase(userId: Long, callback: (List<Order>) -> Unit) {
        val orders: MutableList<Order> = mutableListOf()

        reference.orderByChild("userId").equalTo(userId.toDouble()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                orders.clear()
                for (orderSnapshot in snapshot.children) {
                    val order = orderSnapshot.getValue(Order::class.java)
                    order?.let {
                        val orderId = order.key // Obtener el ID de la orden
                        itemsForOrder(orderId) { orderItems ->
                            it.orderItems = orderItems
                            orders.add(it)
                            callback(orders)
                        }
                    }
                }
                //callback(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }

    private fun itemsForOrder(orderId: String, callback: (List<OrderItem>) -> Unit) {
        val orderItems: MutableList<OrderItem> = mutableListOf()

        itemReference.orderByChild("orderKey").equalTo(orderId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                orderItems.clear()
                for (itemSnapshot in snapshot.children) {
                    val orderItem = itemSnapshot.getValue(OrderItem::class.java)
                    orderItem?.let {
                        orderItems.add(it)
                    }
                }
                foodDetailsForOrderItems(orderItems) { updatedOrderItems ->
                    callback(updatedOrderItems)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }

    private fun foodDetailsForOrderItems(orderItems: List<OrderItem>, callback: (List<OrderItem>) -> Unit) {
        val updatedOrderItems: MutableList<OrderItem> = mutableListOf()
        var count = 0

        for (orderItem in orderItems) {
            foodService.foodById(orderItem.foodId) { food ->
                orderItem.foodName = food?.name ?: "Unknown Food"
                orderItem.foodImage = food?.image ?: ""
                updatedOrderItems.add(orderItem)

                count++
                if (count == orderItems.size) {
                    callback(updatedOrderItems)
                }
            }
        }
    }


    fun createOrder(order: Order, orderItems: List<OrderItem>) {
        maxIdOrder { maxId ->
            val key = reference.push().key.toString()
            order.key = key
            order.id = maxId + 1
            order.orderDate = System.currentTimeMillis().toString()
            reference.child(key).setValue(order)
            orderItems.forEach {
                createOrderItem(order.key, it.foodId, it.quantity)
            }
        }
    }

    private fun createOrderItem(orderKey: String, foodId: Long, quantity: Int) {
        val orderItem = OrderItem(orderKey = orderKey, foodId = foodId, quantity = quantity)
        val key = itemReference.push().key.toString()
        orderItem.orderKey = orderKey
        itemReference.child(key).setValue(orderItem)
    }

    private fun maxIdOrder(callback: (Long) -> Unit) {
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var maxId = 4000L
                for (orderSnapshot in snapshot.children) {
                    val order = orderSnapshot.getValue(Order::class.java)
                    if (order != null && order.id > maxId) {
                        maxId = order.id
                    }
                }
                callback(maxId)
            }
            override fun onCancelled(error: DatabaseError) {
                callback(4000L)
            }
        })
    }
}
