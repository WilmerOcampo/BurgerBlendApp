package com.wo.burgerblend.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wo.burgerblend.R
import com.wo.burgerblend.domain.Category
import com.wo.burgerblend.domain.Food
import com.wo.burgerblend.helper.CartHelper

class DetailFoodActivity : AppCompatActivity() {

    private lateinit var nameFood: TextView
    private lateinit var categoryFood: TextView
    private lateinit var priceFood: TextView
    private lateinit var descriptionFood: TextView
    private lateinit var imageFood: ImageView
    private lateinit var buttonAddToCart: TextView
    private lateinit var quantityOrder: TextView
    private lateinit var buttonPlusOrder: ImageView
    private lateinit var buttonMinusOrder: ImageView
    private lateinit var btnClose: ImageView
    private lateinit var food: Food
    private lateinit var cartHelper: CartHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_food)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        cartHelper = CartHelper(this)
        initView()
        bindDetailFoodView()
    }

    private fun bindDetailFoodView() {
        food = intent.getSerializableExtra("food") as Food

        nameFood.text = food.name

        categories { categories ->
            val category = categories.find { it.id == food.category }
            categoryFood.text = category?.name
                ?: "Categoría desconocida" // Si existe muestra nombre de la categoría
        }

        priceFood.text = "S/. " + food.price.toString()
        descriptionFood.text = food.description
        Glide.with(this)
            .load(food.image) // Carga imagen desde la URL
            .placeholder(R.drawable.veggie_burger) // La imagen tarde en cargar
            .error(R.drawable.veggie_burger) // No se pueda cargar la imagen
            .into(imageFood)

        buttonPlusOrder.setOnClickListener {
            val quantity = quantityOrder.text.toString().toInt()
            quantityOrder.text = (quantity + 1).toString()
        }

        buttonMinusOrder.setOnClickListener {
            val quantity = quantityOrder.text.toString().toInt()
            if (quantity > 1) {
                quantityOrder.text = (quantity - 1).toString()
            }
        }

        buttonAddToCart.setOnClickListener {
            val quantity = quantityOrder.text.toString().toInt()
            food.quantity = quantity
            cartHelper.addToCart(food)
        }

        btnClose.setOnClickListener {
            finish()
        }
    }

    private fun initView() {
        nameFood = findViewById(R.id.textView_nameFoodDetail)
        categoryFood = findViewById(R.id.textView_categoryFoodDetail)
        priceFood = findViewById(R.id.textView_sPriceFoodDetail)
        descriptionFood = findViewById(R.id.textView_descriptionFoodDetail)
        imageFood = findViewById(R.id.imageView_imageFoodDetail)
        buttonAddToCart = findViewById(R.id.textView_buttonAddToCartFoodDetail)
        quantityOrder = findViewById(R.id.textView_quantityOrderFoodDetail)
        buttonPlusOrder = findViewById(R.id.imageView_buttonPlusOrderFoodDetail)
        buttonMinusOrder = findViewById(R.id.imageView_buttonMinusOrderFoodDetail)
        btnClose = findViewById(R.id.imageView_buttonCloseFoodDetail)
    }

    private fun categories(callback: (List<Category>) -> Unit) {
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
                }
                callback(categories)
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores
                callback(categories)
            }
        })
    }
}
