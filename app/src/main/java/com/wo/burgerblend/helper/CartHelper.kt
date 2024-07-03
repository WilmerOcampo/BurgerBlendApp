package com.wo.burgerblend.helper

import android.content.Context
import android.widget.Toast
import com.wo.burgerblend.domain.Food
import com.wo.burgerblend.domain.Order
import com.wo.burgerblend.domain.OrderItem
import com.wo.burgerblend.service.OrderService
import com.wo.burgerblend.service.UserService

class CartHelper(private val context: Context) {
    private val storageHelper = StorageHelper(context)
    private var orderService = OrderService()

    private val userService = UserService()

    fun addToCart(food: Food) {
        val cart = getCart()
        var found = false
        var index = 0
        for (i in cart.indices) {
            if (cart[i].name == food.name) {
                found = true
                index = i
                break
            }
        }
        if (found) {
            cart[index].quantity = food.quantity
        } else {
            cart.add(food)
        }
        storageHelper.putListObject("cart", cart)
        Toast.makeText(context, "Agregado al carrito", Toast.LENGTH_SHORT).show()
    }

    fun plusQuantity(foods: ArrayList<Food>, position: Int) {
        foods[position].quantity += 1
        storageHelper.putListObject("cart", foods)
    }

    fun minusQuantity(foods: ArrayList<Food>, position: Int) {
        if (foods[position].quantity == 1) {
            foods.removeAt(position)
        } else {
            foods[position].quantity -= 1
        }
        storageHelper.putListObject("cart", foods)
    }

    fun getCart(): ArrayList<Food> {
        return storageHelper.getListObject("cart")
    }

    fun getTotalPrice(): Double {
        val cart = getCart()
        var totalPrice = 0.0
        for (food in cart) {
            totalPrice += food.price * food.quantity
        }
        return totalPrice
    }

    fun getIgv(): Double {
        return getTotalPrice() * 0.18
    }

    fun getTotalOrderPrice(): Double {
        return getTotalPrice() - getIgv()
    }

    fun checkOut() {
        userService.currentUserDetails { user ->
            if (user != null) {
                val userId = user.id
                val orderItems: ArrayList<OrderItem> = ArrayList()
                val cart = getCart()
                for (item in cart) {
                    val orderItem =
                        OrderItem(orderKey = "", foodId = item.id, quantity = item.quantity)
                    orderItems.add(orderItem)
                }
                val order = Order(
                    key = "",
                    id = 0,
                    userId = userId,
                    orderDate = "",
                    total = getTotalOrderPrice()
                )
                orderService.createOrder(order, orderItems)
                clearCart()
            } else {
                Toast.makeText(context, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearCart() {
        storageHelper.putListObject("cart", ArrayList<Food>())
    }
}
