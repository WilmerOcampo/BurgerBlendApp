package com.wo.burgerblend.service

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wo.burgerblend.database.DatabaseHelper
import com.wo.burgerblend.domain.Category
import com.wo.burgerblend.utils.Utils

class CategoryService {
    private val database = FirebaseDatabase.getInstance()
    private val reference = database.getReference("categories")
    private val sqlLite = DatabaseHelper()

    fun categories(callback: (List<Category>) -> Unit) {
        if (Utils.isNetworkAvailable()) {
            readFromFirebase(callback)
        } else {
            readFromSQLite(callback)
        }
    }

    private fun readFromFirebase(callback: (List<Category>) -> Unit) {
        val categories: MutableList<Category> = mutableListOf()

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categories.clear()
                for (categorySnapshot in snapshot.children) {
                    val category = categorySnapshot.getValue(Category::class.java)
                    category?.let {
                        categories.add(it)
                    }
                }
                callback(categories)
                saveCategoriesToSQLite(categories)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }

    private fun readFromSQLite(callback: (List<Category>) -> Unit) {
        val categories = sqlLite.categories()
        callback(categories)
    }

    private fun saveCategoriesToSQLite(categories: List<Category>) {
        sqlLite.deleteCategories()
        sqlLite.saveCategories(categories)
    }
}
