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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.wo.burgerblend.R
import com.wo.burgerblend.UserViewModel
import com.wo.burgerblend.activity.CartActivity
import com.wo.burgerblend.activity.food.MenuActivity
import com.wo.burgerblend.authentication.LoginActivity
import com.wo.burgerblend.service.UserService
import jp.wasabeef.glide.transformations.CropCircleTransformation

class ProfileActivity : AppCompatActivity() {
    private lateinit var imageViewProfile: ImageView
    private lateinit var userName: TextView
    private lateinit var userAddress: TextView
    private lateinit var userEmail: TextView
    private lateinit var userPhone: TextView
    private lateinit var btnLogout: TextView
    private lateinit var btnEmptyLogin: TextView
    private lateinit var scrollView: ScrollView

    private val userViewModel: UserViewModel by viewModels()
    private var userService = UserService(this)


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
        bindingProfileView()
        emptyLogin()
        navigate()
    }

    private fun bindingProfileView() {
        userViewModel.user.observe(this, Observer { user ->
            user?.let {
                displayUserInfo(it)
            }
        })

        btnLogout.setOnClickListener {
            userViewModel.signOut()
            finish()
        }
        btnEmptyLogin.setOnClickListener {
            finish()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun displayUserInfo(user: FirebaseUser) {
        userService.userByUid(user.uid) { detailUser ->
            detailUser?.let { userDetails ->
                userName.text = (userDetails.name + ' ' + userDetails.lastname)
                userAddress.text = userDetails.address
                userEmail.text = userDetails.email
                userPhone.text = userDetails.phone

                val requestOptions = RequestOptions.bitmapTransform(CropCircleTransformation())
                userDetails.image.let {
                    Glide.with(this)
                        .load(it)
                        .placeholder(R.drawable.default_profile)
                        .error(R.drawable.default_profile)
                        .apply(requestOptions)
                        .into(imageViewProfile)
                }
            }
        }
    }

    private fun emptyLogin() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            btnEmptyLogin.visibility = View.VISIBLE
            scrollView.visibility = View.GONE
        } else {
            btnEmptyLogin.visibility = View.GONE
            scrollView.visibility = View.VISIBLE
        }
    }

    private fun initView() {
        imageViewProfile = findViewById(R.id.imageView_userProfile)
        userName = findViewById(R.id.nameUserProfile)
        userAddress = findViewById(R.id.addressUserProfile)
        userEmail = findViewById(R.id.emailUserProfile)
        userPhone = findViewById(R.id.phoneUserProfile)
        btnLogout = findViewById(R.id.buttonLogout)
        btnEmptyLogin = findViewById(R.id.textView_buttonLoginProf)
        scrollView = findViewById(R.id.scrollView_profile)
    }

    private fun navigate() {
        val btnHome: LinearLayout = findViewById(R.id.linearLayout_homeAppButtonProf)
        btnHome.setOnClickListener {
            finish()
        }
        val btnCart: FloatingActionButton = findViewById(R.id.floatingActionButton_shoppingCartProf)
        btnCart.setOnClickListener {
            finish()
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
        val btnMenu: LinearLayout = findViewById(R.id.linearLayout_productsAppButtonProf)
        btnMenu.setOnClickListener {
            finish()
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}
