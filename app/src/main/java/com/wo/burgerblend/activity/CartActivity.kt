package com.wo.burgerblend.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wo.burgerblend.R
import com.wo.burgerblend.activity.user.ProfileActivity
import com.wo.burgerblend.adapter.CartAdapter
import com.wo.burgerblend.helper.CartHelper
import kotlin.properties.Delegates

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerViewCart: RecyclerView
    private lateinit var scrollView: ScrollView
    private lateinit var cartHelper: CartHelper

    private lateinit var totalOrderPrice: TextView
    private lateinit var deliveryPrice: TextView
    private lateinit var igvPrice: TextView
    private lateinit var totalPrice: TextView
    private lateinit var emptyCart: TextView
    private lateinit var buttonCheckout: TextView

    private var totalOrder by Delegates.notNull<Double>()

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
        checkOut()
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

        totalOrder = cartHelper.getTotalOrderPrice()
    }

    private fun navigate() {
        val btnHome: LinearLayout = findViewById(R.id.linearLayout_homeAppButtonCart)
        val btnProfile: LinearLayout = findViewById(R.id.linearLayout_profileAppButtonCart)

        btnHome.setOnClickListener {
            finish()
        }
        btnProfile.setOnClickListener {
            finish()
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun bindRecyclerViewCart() {
        recyclerViewCart.apply {
            layoutManager =
                LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
            adapter = CartAdapter(cartHelper.getCart()) {
                updateCartData()
                updateEmptyCartView()
            }
        }

        deliveryPrice.text = "Gratuito"
        updateCartData()
        updateEmptyCartView()
    }

    private fun updateCartData() {
        val igv = cartHelper.getIgv()
        val total = cartHelper.getTotalPrice()

        totalOrderPrice.text = "S/. %.2f".format(totalOrder)
        igvPrice.text = "S/. %.2f".format(igv)
        totalPrice.text = "S/. %.2f".format(total)
    }

    private fun updateEmptyCartView() {
        if (cartHelper.getCart().isEmpty()) {
            emptyCart.visibility = View.VISIBLE
            scrollView.visibility = View.GONE
        } else {
            emptyCart.visibility = View.GONE
            scrollView.visibility = View.VISIBLE
        }
    }

    private fun checkOut() {
        buttonCheckout.setOnClickListener {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Confirmar Pago")
            alert.setMessage("¿Desea pagar S/. %.2f?".format(totalOrder))
            alert.setPositiveButton("Aceptar") { _, _ ->
                cartHelper.checkOut()
                Toast.makeText(this, "Pago Exitoso", Toast.LENGTH_SHORT).show()
                finish()
            }
            alert.setNegativeButton("Cancelar") { _, _ -> }
            alert.show()
        }
    }
}
