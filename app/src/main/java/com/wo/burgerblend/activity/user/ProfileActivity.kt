package com.wo.burgerblend.activity.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wo.burgerblend.R
import com.wo.burgerblend.UserViewModel
import com.wo.burgerblend.activity.CartActivity
import com.wo.burgerblend.activity.MainActivity
import com.wo.burgerblend.activity.SettingsActivity
import com.wo.burgerblend.activity.food.MenuActivity
import com.wo.burgerblend.authentication.LoginActivity
import com.wo.burgerblend.domain.User
import jp.wasabeef.glide.transformations.CropCircleTransformation

class ProfileActivity : AppCompatActivity() {
    private lateinit var imageViewProfile: ImageView
    private lateinit var userName: TextView
    private lateinit var userAddress: TextView
    private lateinit var userEmail: TextView
    private lateinit var userPhone: TextView
    private lateinit var btnLogout: TextView
    private lateinit var btnEmptyLogin: LinearLayout
    private lateinit var scrollView: ScrollView

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
        navigate()
        bindingProfileView()
    }

    private fun initView() {
        imageViewProfile = findViewById(R.id.imageView_userProfile)
        userName = findViewById(R.id.nameUserProfile)
        userAddress = findViewById(R.id.addressUserProfile)
        userEmail = findViewById(R.id.emailUserProfile)
        userPhone = findViewById(R.id.phoneUserProfile)
        btnLogout = findViewById(R.id.textView_buttonLogout)
        btnEmptyLogin = findViewById(R.id.linearLayout_buttonLogin)
        scrollView = findViewById(R.id.scrollView_profile)
    }

    private fun profileView(userDetails: User) {
        userName.text = "${userDetails.name} ${userDetails.lastname}"
        userAddress.text = userDetails.address
        userEmail.text = userDetails.email
        userPhone.text = userDetails.phone

        val requestOptions = RequestOptions.bitmapTransform(CropCircleTransformation())
        Glide.with(this)
            .load(userDetails.image)
            .placeholder(R.drawable.default_profile)
            .error(R.drawable.default_profile)
            .apply(requestOptions)
            .into(imageViewProfile)
    }

    private fun bindingProfileView() {
        userViewModel.userDetails.observe(this, Observer { userDetails ->
            userDetails?.let {
                profileView(it)
            }
        })

        userViewModel.firebaseUser.observe(this, Observer { firebaseUser ->
            if (firebaseUser == null) {
                emptyLogin()
            }
        })

        btnLogout.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de cerrar sesión?")
                .setPositiveButton("Sí") { _, _ ->
                    userViewModel.signOut()
                    AlertDialog.Builder(this)
                        .setTitle("¡Sesión cerrada!")
                        .setPositiveButton("Aceptar") { dialog, _ ->
                            dialog.dismiss()
                            finish()
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                        .show()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            alertDialog.show()
        }

        btnEmptyLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun emptyLogin() {
        btnEmptyLogin.visibility = View.VISIBLE
        scrollView.visibility = View.GONE
    }

    private fun navigate() {
        val btnHome: LinearLayout = findViewById(R.id.linearLayout_homeAppButtonHome)
        btnHome.setOnClickListener {
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val btnCart: FloatingActionButton = findViewById(R.id.floatingActionButton_shoppingCartHome)
        btnCart.setOnClickListener {
            finish()
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
        val btnMenu: LinearLayout = findViewById(R.id.linearLayout_productsAppButtonHome)
        btnMenu.setOnClickListener {
            finish()
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
        val btnSettings: LinearLayout = findViewById(R.id.linearLayout_settingsAppButton)
        btnSettings.setOnClickListener {
            finish()
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}
