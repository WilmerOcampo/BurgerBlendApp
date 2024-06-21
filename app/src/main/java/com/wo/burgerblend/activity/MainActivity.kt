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
        return listOf(
            Category(1, "Clásicas", "cat_2"),
            Category(2, "Vegetales", "cat_2"),
            Category(3, "De Pollo", "cat_2"),
            Category(4, "Postres", "cat_2"),
            Category(5, "Complementos", "cat_2")
        )
    }

    private fun getPopularFoods(): List<Food> {
        return listOf(
            Food(1, "Hamburguesa1", "Carne de res, queso gouda, salsa especial, lechuga, tomate", 19.99, "pop_2", 1),
            Food(2, "Hamburguesa2", "Carne de res, queso gouda, salsa especial, lechuga, tomate", 22.99, "pop_2", 1),
            Food(3, "Hamburguesa3", "Carne de res, queso gouda, salsa especial, lechuga, tomate", 16.99, "pop_2", 1),
            Food(4, "Hamburguesa4", "Carne de res, queso gouda, salsa especial, lechuga, tomate", 25.99, "pop_2", 1),
            Food(5, "Hamburguesa5", "Carne de res, queso gouda, salsa especial, lechuga, tomate", 29.99, "pop_2", 1),
            Food(6, "Hamburguesa6", "Carne de res, queso gouda, salsa especial, lechuga, tomate", 32.99, "pop_2", 1)
        )
    }
}
