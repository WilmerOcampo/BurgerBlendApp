package com.wo.burgerblend.service

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wo.burgerblend.domain.Food

class FoodServiceImpl: IFoodService {
    private val database = FirebaseDatabase.getInstance()
    private val reference = database.getReference("foods")
    override fun foods(callback: (List<Food>) -> Unit) {
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


/*
private val database = FirebaseDatabase.getInstance()
private val reference = database.getReference("foods")

override fun foods(callback: (List<Food>) -> Unit) {
    val foods: MutableList<Food> = mutableListOf()
    reference.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            foods.clear()
            if (snapshot.exists()) {
                for (foodSnapshot in snapshot.children) {
                    try {
                        val food = foodSnapshot.getValue(Food::class.java)
                        if (food != null) {
                            foods.add(food)
                        } else {
                            // Log cuando food es nulo
                            Log.e("FoodServiceImpl", "Food es nulo para snapshot: ${foodSnapshot.key}")
                        }
                    } catch (e: DatabaseException) {
                        e.printStackTrace()
                        Log.e("FoodServiceImpl", "Error de conversión para snapshot: ${foodSnapshot.key}, error: ${e.message}")
                    }
                }
                callback(foods)
            } else {
                callback(emptyList())
            }
        }

        override fun onCancelled(error: DatabaseError) {
            // Maneja los errores
            callback(emptyList())
        }
    })*/
