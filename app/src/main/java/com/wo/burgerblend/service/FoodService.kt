package com.wo.burgerblend.service

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wo.burgerblend.database.DatabaseHelper
import com.wo.burgerblend.domain.Food
import com.wo.burgerblend.utils.Utils

class FoodService {
    private val database = FirebaseDatabase.getInstance()
    private val reference = database.getReference("foods")
    private val sqlLite = DatabaseHelper()

    fun foods(callback: (List<Food>) -> Unit) {
        if (Utils.isNetworkAvailable()) {
            readFromFirebase(callback)
        } else {
            readFromSQLite(callback)
        }
    }

    private fun readFromFirebase(callback: (List<Food>) -> Unit) {
        val foods: MutableList<Food> = mutableListOf()

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                foods.clear()
                if (snapshot.exists()) {
                    for (foodSnapshot in snapshot.children) {
                        val food = foodSnapshot.getValue(Food::class.java)
                        food?.let {
                            foods.add(it)
                        }
                    }
                    callback(foods)
                    saveFoodsToSQLite(foods)
                } else {
                    callback(emptyList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
                callback(emptyList())
            }
        })
    }

    fun foodById(foodId: Long, callback: (Food?) -> Unit) {
        reference.orderByChild("id").equalTo(foodId.toDouble()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val food = snapshot.children.first().getValue(Food::class.java)
                    callback(food)
                } else {
                    callback(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }

    private fun readFromSQLite(callback: (List<Food>) -> Unit) {
        val foods = sqlLite.foods()
        callback(foods)
    }

    private fun saveFoodsToSQLite(foods: List<Food>) {
        sqlLite.deleteFoods()
        sqlLite.saveFoods(foods)
    }
}
