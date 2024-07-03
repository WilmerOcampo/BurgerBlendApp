package com.wo.burgerblend.activity.food

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wo.burgerblend.R
import com.wo.burgerblend.activity.CartActivity
import com.wo.burgerblend.activity.MainActivity
import com.wo.burgerblend.activity.SettingsActivity
import com.wo.burgerblend.activity.user.ProfileActivity
import com.wo.burgerblend.adapter.CategoryAdapter
import com.wo.burgerblend.adapter.FoodAdapter
import com.wo.burgerblend.domain.Food
import com.wo.burgerblend.service.CategoryService
import com.wo.burgerblend.service.FoodService

class MenuActivity : AppCompatActivity(), CategoryAdapter.ItemClickListener {
    private lateinit var txtSearch: EditText

    private var foodService = FoodService()
    private var categoryService = CategoryService()
    private var foodAdapter = FoodAdapter(emptyList())
    private var foodList: List<Food> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupRecyclerViews()
        setupSearch()
        navigate()
    }

    private fun initViews() {
        txtSearch = findViewById(R.id.editText_searchMenusMenu)
    }

    private fun setupRecyclerViews() {
        val recyclerViewMenuItems: RecyclerView = findViewById(R.id.recyclerView_popularFood)
        val recyclerViewCategories: RecyclerView = findViewById(R.id.recyclerView_category)

        categoryService.categories { categories ->
            setupRecyclerView(
                recyclerViewCategories,
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false),
                CategoryAdapter(categories, this)
            )
        }

        /*foodService.foods { foods ->
            setupRecyclerView(
                recyclerViewMenuItems,
                GridLayoutManager(this, 2),
                FoodAdapter(foods)
            )
        }*/
        foodService.foods { foods ->
            this.foodList = foods
            foodAdapter.foods(foods)
        }
        setupRecyclerView(
            recyclerViewMenuItems,
            GridLayoutManager(this, 2),
            foodAdapter
        )

        setupRecyclerView(
            recyclerViewMenuItems,
            GridLayoutManager(this, 2),
            foodAdapter
        )
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
        val btnHome: LinearLayout = findViewById(R.id.linearLayout_homeAppButtonHome)
        btnHome.setOnClickListener {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
        val btnCart: FloatingActionButton = findViewById(R.id.floatingActionButton_shoppingCartHome)
        btnCart.setOnClickListener {
            finish()
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
        val btnProfile: LinearLayout = findViewById(R.id.linearLayout_profileAppButton)
        btnProfile.setOnClickListener {
            finish()
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        val btnSettings: LinearLayout = findViewById(R.id.linearLayout_settingsAppButton)
        btnSettings.setOnClickListener {
            finish()
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun setupSearch() {
        txtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterFoods(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterFoods(query: String) {
        val filteredList = foodList.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
        }
        foodAdapter.foods(filteredList)
    }

    override fun onItemClick(categoryId: Long) {
        val filteredList = foodList.filter {
            it.category == categoryId
        }
        foodAdapter.foods(filteredList)
    }
}
