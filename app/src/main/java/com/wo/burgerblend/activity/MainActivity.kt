package com.wo.burgerblend.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wo.burgerblend.R
import com.wo.burgerblend.adapter.CategoryAdapter
import com.wo.burgerblend.adapter.FoodAdapter
import com.wo.burgerblend.domain.Category
import com.wo.burgerblend.domain.Food

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewCategory: RecyclerView
    private lateinit var recyclerViewPopularFood: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupRecyclerViews()
        navigate()
    }

    private fun navigate() {
        val btnFloatActionCart: FloatingActionButton = findViewById(R.id.floatingActionButton_shoppingCartHome)
        /*val btnHome: LinearLayout = findViewById(R.id.linearLayout_homeAppButton)*/

        btnFloatActionCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        /*if (this !is MainActivity) {
            btnHome.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }*/
    }

    private fun setupRecyclerViews() {
        recyclerViewCategory = findViewById(R.id.recyclerView_category)
        recyclerViewPopularFood = findViewById(R.id.recyclerView_popularFood)

        setupRecyclerView(
            recyclerViewCategory,
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false),
            CategoryAdapter(getCategories())
        )

        setupRecyclerView(
            recyclerViewPopularFood,
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false),
            FoodAdapter(getPopularFoods())
        )
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, layoutManager: LinearLayoutManager, adapter: RecyclerView.Adapter<*>) {
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun getCategories(): List<Category> {
        val categories: MutableList<Category> = mutableListOf()
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("categories")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categories.clear()
                if (snapshot.exists()) {
                    for (categorySnapshot in snapshot.children) {
                        val category = categorySnapshot.getValue(Category::class.java)
                        if (category != null) {
                            categories.add(category)
                        }
                    }
                    Log.d("Categories", categories.toString())
                    recyclerViewCategory.adapter = CategoryAdapter(categories)
                } else {
                    Toast.makeText(this@MainActivity, "No hay datos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores
            }
        })
        return categories
    }

    private fun getPopularFoods(): List<Food> {
        val foods: MutableList<Food> = mutableListOf()

        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("foods")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                foods.clear()
                if (snapshot.exists()) {
                    for (foodSnapshot in snapshot.children) {
                        val food = foodSnapshot.getValue(Food::class.java)
                        if (food != null) {
                            foods.add(food)
                        }
                    }
                    recyclerViewPopularFood.adapter = FoodAdapter(foods)
                } else {
                    Toast.makeText(this@MainActivity, "No hay datos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores
            }
        })
        return foods
    }
}
