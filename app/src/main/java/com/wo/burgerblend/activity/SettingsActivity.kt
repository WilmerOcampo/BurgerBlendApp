package com.wo.burgerblend.activity

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wo.burgerblend.R
import com.wo.burgerblend.UserViewModel
import com.wo.burgerblend.activity.food.MenuActivity
import com.wo.burgerblend.activity.user.ProfileActivity
import com.wo.burgerblend.authentication.LoginActivity

class SettingsActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        navigate()
    }

    private fun navigate() {
        val btnFloatActionCart: FloatingActionButton =
            findViewById(R.id.floatingActionButton_shoppingCartHome)

        btnFloatActionCart.setOnClickListener {
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
            startActivity(Intent(this, MainActivity::class.java))
        }

        val btnLogout: TextView = findViewById(R.id.textView_buttonLogout)
        if (userViewModel.firebaseUser.value == null) {
            btnLogout.text = "Iniciar Sesión"
            btnLogout.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        } else {
            btnLogout.text = "Cerrar Sesión"
            btnLogout.setOnClickListener {
                val alertDialog = android.app.AlertDialog.Builder(this)
                    .setTitle("Cerrar sesión")
                    .setMessage("¿Estás seguro de cerrar sesión?")
                    .setPositiveButton("Sí") { _, _ ->
                        userViewModel.signOut()
                        AlertDialog.Builder(this)
                            .setTitle("¡Sesión cerrada!")
                            .setPositiveButton("Aceptar") { dialog, _ ->
                                dialog.dismiss()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            .show()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                alertDialog.show()
            }
        }
    }
}