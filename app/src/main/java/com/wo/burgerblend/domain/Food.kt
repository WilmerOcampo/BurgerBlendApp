package com.wo.burgerblend.domain

import java.io.Serializable

class Food(var id: Long, var name: String, var description: String, var price: Double, var image: String, var quantity: Int): Serializable {
}
