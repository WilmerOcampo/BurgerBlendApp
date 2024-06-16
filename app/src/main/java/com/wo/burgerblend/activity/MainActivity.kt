package com.wo.burgerblend.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wo.burgerblend.R
import com.wo.burgerblend.adapter.CategoriaAdapter
import com.wo.burgerblend.adapter.FoodAdapter
import com.wo.burgerblend.domain.Categoria
import com.wo.burgerblend.domain.Food

class MainActivity : AppCompatActivity() {

    private var adapter: RecyclerView.Adapter<*>? = null
    private var recyclerViewCategorias: RecyclerView? = null
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
        recyclerViewCategoria()
        recyclerViewPopularFood()
    }

    private fun recyclerViewCategoria() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewCategorias = findViewById(R.id.recyclerView_categoria)
        recyclerViewCategorias!!.layoutManager = linearLayoutManager

        val categorias: ArrayList<Categoria> = ArrayList()
        categorias.add(Categoria(1, "Pizzas", "cat_1"))
        categorias.add(Categoria(2, "Hamburguesas", "cat_2"))
        categorias.add(Categoria(3, "Hot Dogs", "cat_3"))
        categorias.add(Categoria(4, "Bebidas", "cat_4"))
        categorias.add(Categoria(5, "Donas", "cat_5"))

        adapter = CategoriaAdapter(categorias)
        recyclerViewCategorias!!.adapter = adapter
    }

    private fun recyclerViewPopularFood(){
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewPopularFood = findViewById(R.id.recyclerView_popularFood)
        recyclerViewPopularFood!!.layoutManager = linearLayoutManager

        val foods: ArrayList<Food> = ArrayList()
        foods.add(Food(1, "Pizza Pepperoni", "Rebanadas de pepperoni, queso mozzarella, rodajas de tomate, rodajas de albahaca, oregano fresco, pimienta negra molida, salsa.", 19.99, "pizza"))
        foods.add(Food(2, "Hamburguesa con Queso", "Carne de res, queso gouda, salsa especil, lechuga, tomate", 19.99, "pop_2"))
        foods.add(Food(3, "Hot Dog", "Carne de res, queso gouda, salsa especil, lechuga, tomate", 19.99, "hotdog"))
        foods.add(Food(4, "Pizza Vegetariana", "Aceite oliva, aceite vegetal, kalamata sin hueso, tomates cherry, oregano freso, albahaca.", 22.90, "pop_3"))
        foods.add(Food(5, "Donas", "Donas de chocolate", 19.99, "donas"))

        adapter = FoodAdapter(foods)
        recyclerViewPopularFood!!.adapter = adapter


    }
}