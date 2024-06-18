package com.wo.burgerblend.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wo.burgerblend.R
import com.wo.burgerblend.adapter.CartAdapter
import com.wo.burgerblend.domain.Food
import com.wo.burgerblend.helper.CartHelper

class CartActivity : AppCompatActivity() {

    private var adapter: RecyclerView.Adapter<*>? = null
    private var recyclerViewCart: RecyclerView? = null
    private var scrollView: ScrollView? = null
    private var cartHelper: CartHelper? = null

    private var totalOrderPrice: TextView? = null
    private var deliveryPrice: TextView? = null
    private var igvPrice: TextView? = null
    private var totalPrice: TextView? = null
    private var emptyCart: TextView? = null
    private var buttonCheckout: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cartHelper = CartHelper(this)
        initView()
        bindRecyclerViewCart()
        navigate()
    }

    private fun initView() {
        recyclerViewCart = findViewById(R.id.recyclerView_cart)
        scrollView = findViewById(R.id.scrollView_cart)
        totalOrderPrice = findViewById(R.id.textView_totalOrderPriceCart)
        deliveryPrice = findViewById(R.id.textView_deliveryPriceCart)
        igvPrice = findViewById(R.id.textView_igvPriceCart)
        totalPrice = findViewById(R.id.textView_totalPriceCart)
        emptyCart = findViewById(R.id.textView_emptyCart)
        buttonCheckout = findViewById(R.id.textView_buttonCheckoutCart)
    }

    private fun navigate() {
        val btnFloatActionCart: FloatingActionButton = findViewById(R.id.floatingActionButton_cartCart)
        val btnHome: LinearLayout = findViewById(R.id.linearLayout_homeAppButtonCart)

        if(this !is CartActivity){
            btnFloatActionCart.setOnClickListener {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
            }
        }
        btnHome.setOnClickListener {
            finish()
        }
    }

    private fun bindRecyclerViewCart(){
        intent.getSerializableExtra("food") as Food?
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewCart?.layoutManager = layoutManager
        adapter = CartAdapter(cartHelper!!.getCart()){
            updateCartData()
        }

        deliveryPrice?.text = "Gratuito"
        updateCartData()

        if (cartHelper!!.getCart().isEmpty()) {
            emptyCart?.visibility = View.VISIBLE
            scrollView?.visibility = View.GONE
        } else {
            emptyCart?.visibility = View.GONE
            scrollView?.visibility = View.VISIBLE
        }
        recyclerViewCart?.adapter = adapter
    }

    private fun updateCartData() {
        val totalOrder = cartHelper?.getTotalOrderPrice() ?: 0.0
        val igv = cartHelper?.getIgv() ?: 0.0
        val total = cartHelper?.getTotalPrice() ?: 0.0

        totalOrderPrice?.text = "S/. " + String.format("%.2f", totalOrder)
        igvPrice?.text = "S/. " + String.format("%.2f", igv)
        totalPrice?.text = "S/. " + String.format("%.2f", total)

    }
}
