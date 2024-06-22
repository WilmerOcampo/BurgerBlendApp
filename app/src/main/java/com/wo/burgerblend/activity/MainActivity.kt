package com.wo.burgerblend.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wo.burgerblend.R
import com.wo.burgerblend.adapter.CategoryAdapter
import com.wo.burgerblend.adapter.FoodAdapter
import com.wo.burgerblend.service.CategoryServiceImpl
import com.wo.burgerblend.service.FoodServiceImpl
import com.wo.burgerblend.service.ICategoryService
import com.wo.burgerblend.service.IFoodService

class MainActivity : AppCompatActivity() {

    private var foodService = FoodServiceImpl() as IFoodService
    private var categoryService = CategoryServiceImpl(this) as ICategoryService

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

        btnFloatActionCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerViews() {
        val recyclerViewCategory: RecyclerView = findViewById(R.id.recyclerView_category)
        val recyclerViewPopularFood: RecyclerView = findViewById(R.id.recyclerView_popularFood)

        categoryService.categories { categories ->
            setupRecyclerView(
                recyclerViewCategory,
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false),
                CategoryAdapter(categories)
            )
        }

        foodService.foods { foods ->
            setupRecyclerView(
                recyclerViewPopularFood,
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false),
                FoodAdapter(foods)
            )
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, layoutManager: LinearLayoutManager, adapter: RecyclerView.Adapter<*>) {
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }
}
