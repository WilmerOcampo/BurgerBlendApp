package com.wo.burgerblend.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wo.burgerblend.database.DatabaseHelper
import com.wo.burgerblend.domain.Category

class CategoryService(private val context: Context) {
    private val database = FirebaseDatabase.getInstance()
    private val reference = database.getReference("categories")
    private val sqlLite = DatabaseHelper(context)

    fun categories(callback: (List<Category>) -> Unit) {
        if (isNetworkAvailable()) {
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

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val capabilities =
            connectivityManager.getNetworkCapabilities(network)
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }
}
