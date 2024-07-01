package com.wo.burgerblend.domain

import java.io.Serializable

class OrderItem(var orderKey: String, var foodId: Long, var quantity: Int): Serializable {
    constructor() : this("", 0, 0)
}