package com.wo.burgerblend.domain

import java.io.Serializable

class User(var key:String, var uid:String, var id:Long, var name:String, var lastname:String, var image:String, var email:String, var address:String, var phone:String, var role:String, var password:String, var active:Boolean): Serializable {
    constructor() : this("", "", 0, "", "", "", "", "", "", "", "", true)

}
