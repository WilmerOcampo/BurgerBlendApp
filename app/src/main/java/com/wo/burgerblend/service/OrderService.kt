package com.wo.burgerblend.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wo.burgerblend.domain.Order
import com.wo.burgerblend.domain.OrderItem


class OrderService(private val context: Context) {
    private val reference = FirebaseDatabase.getInstance().getReference("orders")
    private val itemReference = FirebaseDatabase.getInstance().getReference("orderItems")

    fun orders(callback: (List<Order>) -> Unit) {
        if (isNetworkAvailable()) {
            ordersFirebase(callback)
        } else {
            //usersSQLite(callback)
        }
    }

    private fun ordersFirebase(callback: (List<Order>) -> Unit) {
        val orders: MutableList<Order> = mutableListOf()

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                orders.clear()
                for (userSnapshot in snapshot.children) {
                    val order = userSnapshot.getValue(Order::class.java)
                    order?.let {
                        orders.add(it)
                    }
                }
                callback(orders)
                //saveUsersToSQLite(users)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }

    fun createOrder(order: Order, orderItems: List<OrderItem>) {
        /*val key = reference.push().key.toString()
        order.key = key
        order.id = maxIdOrder() + 1
        order.orderDate = System.currentTimeMillis().toString()
        reference.child(key).setValue(order)
        orderItems.forEach {
            createOrderItem(order.key, it.foodId, it.quantity)
        }*/
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

    /*private fun maxIdOrder(): Long {
        var maxId = 4000L
        ordersFirebase { orders ->
            if (orders.isNotEmpty())
                maxId = orders.maxOfOrNull { it.id } ?: 4000L
        }
        return maxId
    }*/
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


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val capabilities =
            connectivityManager.getNetworkCapabilities(network)
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }
}
