package com.wo.burgerblend.domain

import java.io.Serializable

class Category(var key:String, var id:Long, var name:String, var description:String, var image:String): Serializable {
    constructor() : this("", 0, "", "", "")
}
