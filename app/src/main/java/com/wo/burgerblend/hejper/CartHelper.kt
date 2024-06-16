package com.wo.burgerblend.hejper

import android.content.Context
import com.wo.burgerblend.domain.Food

class CartHelper(val context: Context, val dataStorageHelper: DataStorageHelper = DataStorageHelper(context)) {
    fun addToCart(food: Food) {
        //dataStorageHelper.addToCart(food)
        //val foods: ArrayList<Food> = getCart()
            val exists: Boolean = true
    }
/*
    fun removeFromCart(food: Food) {
        dataStorageHelper.removeFromCart(food)
    }
    fun getCart(): ArrayList<Food> {
        return dataStorageHelper.getListObject("cart")
    }
    fun clearCart() {
        return dataStorageHelper.clearCart()
    }
    fun getCartSize(): Int {
        return dataStorageHelper.getCartSize()
    }
    fun getCartTotal(): Double {
        return dataStorageHelper.getCartTotal()
    }
    fun
*/




}