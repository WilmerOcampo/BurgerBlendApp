package com.wo.burgerblend.domain

import java.io.Serializable

class OrderItem(var orderKey: String, var foodId: Long, var quantity: Int, var foodName: String? = "", var foodImage: String? = "") : Serializable {
    constructor() : this("", 0, 0)
}
