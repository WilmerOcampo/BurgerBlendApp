package com.wo.burgerblend.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wo.burgerblend.R
import com.wo.burgerblend.activity.food.MenuActivity
import com.wo.burgerblend.activity.user.ProfileActivity

abstract class NavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResourceId())

        navigate()
    }

    protected abstract fun getLayoutResourceId(): Int

    private fun navigate() {
        val btnFloatActionCart: FloatingActionButton = findViewById(R.id.floatingActionButton_shoppingCartHome)

        btnFloatActionCart.setOnClickListener {
            finish()
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        val btnProfile: LinearLayout = findViewById(R.id.linearLayout_profileAppButton)
            btnProfile.setOnClickListener {
            finish()
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val btnMenuItems: LinearLayout = findViewById(R.id.linearLayout_productsAppButtonHome)
            btnMenuItems.setOnClickListener {
            finish()
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        val btnHome: LinearLayout = findViewById(R.id.linearLayout_homeAppButtonHome)
        btnHome.setOnClickListener {
            finish()
        }

        val btnSettings: LinearLayout = findViewById(R.id.linearLayout_settingsAppButton)
        btnSettings.setOnClickListener {
            finish()
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
