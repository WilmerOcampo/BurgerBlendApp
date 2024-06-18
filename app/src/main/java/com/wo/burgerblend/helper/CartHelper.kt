package com.wo.burgerblend.helper

import android.content.Context
import android.widget.Toast
import com.wo.burgerblend.domain.Food

class CartHelper(private val context: Context) {
    private val tinyDB: TinyDB = TinyDB(context)

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
        tinyDB.putListObject("cart", cart)
        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
    }

    fun plusQuantity(foods: ArrayList<Food>, position: Int) {
        foods[position].quantity += 1
        tinyDB.putListObject("cart", foods)
    }

    fun minusQuantity(foods: ArrayList<Food>, position: Int) {
        if (foods[position].quantity == 1) {
            foods.removeAt(position)
        } else {
            foods[position].quantity -= 1
        }
        tinyDB.putListObject("cart", foods)
    }

    fun getCart(): ArrayList<Food> {
        return tinyDB.getListObject("cart")
    }

    fun removeFromCart(food: Food) {
        // Implementar lógica para eliminar del carrito si es necesario
    }

    fun clearCart() {
        // Implementar lógica para limpiar el carrito si es necesario
    }

    fun getCartSize(): Int {
        return getCart().size
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

    fun saveCart() {
        // Implementar lógica para guardar el carrito si es necesario
    }

    fun loadCart() {
        // Implementar lógica para cargar el carrito si es necesario
    }

    fun clearData() {
        // Implementar lógica para limpiar datos si es necesario
    }

    fun initCart() {
        // Implementar lógica para inicializar el carrito si es necesario
    }
}