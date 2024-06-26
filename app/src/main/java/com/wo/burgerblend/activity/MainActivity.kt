package com.wo.burgerblend.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.wo.burgerblend.R
import com.wo.burgerblend.activity.food.MenuActivity
import com.wo.burgerblend.activity.user.ProfileActivity
import com.wo.burgerblend.adapter.CategoryAdapter
import com.wo.burgerblend.adapter.FoodAdapter
import com.wo.burgerblend.service.CategoryService
import com.wo.burgerblend.service.FoodService
import com.wo.burgerblend.service.UserService
import jp.wasabeef.glide.transformations.CropCircleTransformation

class MainActivity : AppCompatActivity() {
    private lateinit var imageProfile: ImageView
    private lateinit var nameUser: TextView

    private var foodService = FoodService()
    private var categoryService = CategoryService(this)

    private var userService = UserService(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
        bindingHomeView()
        setupRecyclerViews()
        navigate()
    }

    private fun initView() {
        imageProfile = findViewById(R.id.imageView_perfilUsuario)
        nameUser = findViewById(R.id.textView_holaUsuario)
    }

    private fun navigate() {
        val btnFloatActionCart: FloatingActionButton =
            findViewById(R.id.floatingActionButton_shoppingCartHome)

        btnFloatActionCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        val btnProfile: LinearLayout = findViewById(R.id.linearLayout_profileAppButtonHome)
        btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val btnMenuItems: LinearLayout = findViewById(R.id.linearLayout_productsAppButtonHome)
        btnMenuItems.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerViews() {
        val recyclerViewCategory: RecyclerView = findViewById(R.id.recyclerView_category)
        val recyclerViewPopularFood: RecyclerView = findViewById(R.id.recyclerView_popularFood)

        categoryService.categories { categories ->
            setupRecyclerView(
                recyclerViewCategory,
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false),
                CategoryAdapter(categories)
            )
        }

        foodService.foods { foods ->
            setupRecyclerView(
                recyclerViewPopularFood,
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false),
                FoodAdapter(foods)
            )
        }
    }

    private fun bindingHomeView() {
        FirebaseAuth.getInstance().addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                clearUserInfo()
            } else {
                userInfo(user)
            }
        }

        imageProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun clearUserInfo() {
        nameUser.text = "Hola: Usuario"
        imageProfile.setImageResource(R.drawable.default_profile)
    }

    private fun userInfo(user: FirebaseUser) {
        userService.userByUid(user.uid) { detailUser ->
            detailUser?.let { userDetails ->
                nameUser.text = ("Hola: " + userDetails.name + ' ' + userDetails.lastname)

                val requestOptions = RequestOptions.bitmapTransform(CropCircleTransformation())
                userDetails.image.let {
                    Glide.with(this)
                        .load(it)
                        .placeholder(R.drawable.default_profile)
                        .error(R.drawable.default_profile)
                        .apply(requestOptions)
                        .into(imageProfile)
                }
            }
        }
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        layoutManager: LinearLayoutManager,
        adapter: RecyclerView.Adapter<*>
    ) {
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }
}
