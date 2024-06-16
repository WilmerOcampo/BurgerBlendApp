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
import com.wo.burgerblend.domain.Categoria

class MainActivity : AppCompatActivity() {

    private var adapter: RecyclerView.Adapter<*>? = null
    private var recyclerViewCategorias: RecyclerView? = null

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
}