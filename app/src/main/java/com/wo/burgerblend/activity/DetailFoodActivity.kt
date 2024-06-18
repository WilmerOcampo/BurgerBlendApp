package com.wo.burgerblend.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.wo.burgerblend.R
import com.wo.burgerblend.domain.Food
import com.wo.burgerblend.helper.CartHelper

class DetailFoodActivity : AppCompatActivity() {

    private var nameFood: TextView? = null
    private var priceFood: TextView? = null
    private var descriptionFood: TextView? = null
    private var imageFood: ImageView? = null
    private var buttonAddToCart: TextView? = null
    private var quantityOrder: TextView? = null
    private var buttonPlusOrder: ImageView? = null
    private var buttonMinusOrder: ImageView? = null
    private var btnClose: ImageView? = null
    private var food: Food? = null
    private var cartHelper: CartHelper? = null

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
        food = intent.getSerializableExtra("food") as Food?

        nameFood?.text = food?.name
        priceFood?.text = "S/. " + food?.price.toString()
        descriptionFood?.text = food?.description
        val drawableResourceId: Int =
            this.resources.getIdentifier(food?.image, "drawable", this.packageName)
        imageFood?.setImageResource(drawableResourceId)

        buttonPlusOrder?.setOnClickListener {
            val quantity = quantityOrder?.text.toString().toInt()
            quantityOrder?.text = (quantity + 1).toString()
        }

        buttonMinusOrder?.setOnClickListener {
            val quantity = quantityOrder?.text.toString().toInt()
            if (quantity > 1) {
                quantityOrder?.text = (quantity - 1).toString()
            }
        }

        buttonAddToCart?.setOnClickListener {
            val quantity = quantityOrder?.text.toString().toInt()
            food?.quantity = quantity
            cartHelper?.addToCart(food!!)
        }

        btnClose?.setOnClickListener {
            finish()
        }
    }

    private fun initView() {
        nameFood = findViewById(R.id.textView_nameFoodDetail)
        priceFood = findViewById(R.id.textView_sPriceFoodDetail)
        descriptionFood = findViewById(R.id.textView_descriptionFoodDetail)
        imageFood = findViewById(R.id.imageView_imageFoodDetail)
        buttonAddToCart = findViewById(R.id.textView_buttonAddToCartFoodDetail)
        quantityOrder = findViewById(R.id.textView_quantityOrderFoodDetail)
        buttonPlusOrder = findViewById(R.id.imageView_buttonPlusOrderFoodDetail)
        buttonMinusOrder = findViewById(R.id.imageView_buttonMinusOrderFoodDetail)
        btnClose = findViewById(R.id.imageView_buttonCloseFoodDetail)
    }
}
