package com.wo.burgerblend.domain

import java.io.Serializable

class Order(
    var key: String,
    var id: Long,
    var userId: Long,
    var orderDate: String,
    var total: Double,
    var orderItems: List<OrderItem> = mutableListOf()
) :
    Serializable {
    constructor() : this("", 0, 0, "", 0.0)
}
