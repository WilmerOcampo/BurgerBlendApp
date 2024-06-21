package com.wo.burgerblend.domain

import java.io.Serializable

class Food(var key: String, var id: Long, var name: String, var description: String, var category: Long, var price: Double, var image: String, var quantity: Int): Serializable {
    constructor() : this("", 0, "", "",  0, 0.0, "", 0)
}
