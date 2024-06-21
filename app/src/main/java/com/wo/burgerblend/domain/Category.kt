package com.wo.burgerblend.domain

class Category(var key:String, var id:Long, var name:String, var description:String, var image:String) {
    constructor() : this("", 0, "", "", "")
}