package com.wo.burgerblend.activity.food

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wo.burgerblend.R
import com.wo.burgerblend.activity.CartActivity
import com.wo.burgerblend.activity.user.ProfileActivity
import com.wo.burgerblend.adapter.CategoryAdapter
import com.wo.burgerblend.adapter.FoodAdapter
import com.wo.burgerblend.service.CategoryService
import com.wo.burgerblend.service.FoodService

class MenuActivity : AppCompatActivity() {
    private var foodService = FoodService()
    private var categoryService = CategoryService(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerViews()
        navigate()
    }

    private fun setupRecyclerViews() {
        val recyclerViewMenuItems: RecyclerView = findViewById(R.id.recyclerView_menuItems)
        val recyclerViewCategories: RecyclerView = findViewById(R.id.recyclerView_categoriesMenu)

        categoryService.categories { categories ->
            setupRecyclerView(
                recyclerViewCategories,
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false),
                CategoryAdapter(categories)
            )
        }

        foodService.foods { foods ->
            setupRecyclerView(
                recyclerViewMenuItems,
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false),
                FoodAdapter(foods)
            )
        }
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        layoutManager: LinearLayoutManager,
        adapter: RecyclerView.Adapter<*>
    ) {
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun navigate() {
        val btnHome: LinearLayout = findViewById(R.id.linearLayout_homeAppButtonMenu)
        btnHome.setOnClickListener {
            finish()
        }
        val btnCart: FloatingActionButton = findViewById(R.id.floatingActionButton_shoppingCartMenu)
        btnCart.setOnClickListener {
            finish()
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
        val btnProfile : LinearLayout = findViewById(R.id.linearLayout_profileAppButtonMenu)
        btnProfile.setOnClickListener {
            finish()
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}