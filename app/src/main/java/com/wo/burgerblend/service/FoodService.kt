package com.wo.burgerblend.service

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wo.burgerblend.domain.Food

class FoodService {
    private val database = FirebaseDatabase.getInstance()
    private val reference = database.getReference("foods")

    fun foods(callback: (List<Food>) -> Unit) {
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
}
