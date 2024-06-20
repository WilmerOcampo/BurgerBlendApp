package com.wo.burgerblend.activity

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
import com.wo.burgerblend.adapter.CategoryAdapter
import com.wo.burgerblend.adapter.FoodAdapter
import com.wo.burgerblend.domain.Category
import com.wo.burgerblend.domain.Food

class MainActivity : AppCompatActivity() {

    private var adapter: RecyclerView.Adapter<*>? = null
    private var recyclerViewCategory: RecyclerView? = null
    private var recyclerViewPopularFood: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerViewCategory()
        recyclerViewPopularFood()
        navigate()
    }

    private fun navigate() {
        val btnFloatActionCart: FloatingActionButton = findViewById(R.id.floatingActionButton_shoppingCartHome)
        val btnHome: LinearLayout = findViewById(R.id.linearLayout_homeAppButton)

        btnFloatActionCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
        if (this !is MainActivity){
            btnHome.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun recyclerViewCategory() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewCategory = findViewById(R.id.recyclerView_category)
        recyclerViewCategory!!.layoutManager = linearLayoutManager

        val categories: ArrayList<Category> = ArrayList()
        categories.add(Category(1, "Clásicas", "cat_2"))
        categories.add(Category(2, "Vegetales", "cat_2"))
        categories.add(Category(3, "De Pollo", "cat_2"))
        categories.add(Category(4, "Postres", "cat_2"))
        categories.add(Category(5, "Complementos", "cat_2"))

        adapter = CategoryAdapter(categories)
        recyclerViewCategory!!.adapter = adapter
    }

    private fun recyclerViewPopularFood(){
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewPopularFood = findViewById(R.id.recyclerView_popularFood)
        recyclerViewPopularFood!!.layoutManager = linearLayoutManager

        val foods: ArrayList<Food> = ArrayList()
        foods.add(Food(1, "Hamburguesa1", "Carne de res, queso gouda, salsa especil, lechuga, tomate", 19.99, "pop_2", 1))
        foods.add(Food(2, "Hamburguesa2", "Carne de res, queso gouda, salsa especil, lechuga, tomate", 22.99, "pop_2", 1))
        foods.add(Food(4, "Hamburguesa4", "Carne de res, queso gouda, salsa especil, lechuga, tomate", 25.99, "pop_2", 1))
        foods.add(Food(3, "Hamburguesa3", "Carne de res, queso gouda, salsa especil, lechuga, tomate", 16.99, "pop_2", 1))
        foods.add(Food(5, "Hamburguesa5", "Carne de res, queso gouda, salsa especil, lechuga, tomate", 29.99, "pop_2", 1))
        foods.add(Food(6, "Hamburguesa6", "Carne de res, queso gouda, salsa especil, lechuga, tomate", 32.99, "pop_2", 1))

        adapter = FoodAdapter(foods)
        recyclerViewPopularFood!!.adapter = adapter
    }
}
