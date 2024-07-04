package com.wo.burgerblend.service

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wo.burgerblend.database.DatabaseHelper
import com.wo.burgerblend.domain.Food
import com.wo.burgerblend.utils.Utils

/**
 * Service class for Food objects
 * */
class FoodService {
    private val database = FirebaseDatabase.getInstance()
    private val reference = database.getReference("foods")
    private val sqlLite = DatabaseHelper()

    /**
     * Read from Firebase database or SQLite database and return a list of Food objects
     * @param callback function to call when the list of Food objects is read
     * */
    fun foods(callback: (List<Food>) -> Unit) {
        if (Utils.isNetworkAvailable()) {
            readFromFirebase(callback)
        } else {
            readFromSQLite(callback)
        }
    }

    /**
     * Read from Firebase database and return a list of Food objects
     * @param callback function to call when the list of Food objects is read
     * **/
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

    /**
     * Read from Firebase database and return a Food object by id or null if not found
     * @param foodId id of the Food object to read
     * @param callback function to call when the Food object is read
     * */
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

    /**
     * Read from SQLite database and return a list of Food objects
     * @param callback function to call when the list of Food objects is read
     * */
    private fun readFromSQLite(callback: (List<Food>) -> Unit) {
        val foods = sqlLite.foods()
        callback(foods)
    }

    /**
     * Save foods to SQLite database and delete all previous data from it to avoid duplicates
     * @param foods list of Food objects to save
     * */
    private fun saveFoodsToSQLite(foods: List<Food>) {
        sqlLite.deleteFoods()
        sqlLite.saveFoods(foods)
    }
}
