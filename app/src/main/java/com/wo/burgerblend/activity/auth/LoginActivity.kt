package com.wo.burgerblend.activity.auth

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.wo.burgerblend.R
import com.wo.burgerblend.UserViewModel
import com.wo.burgerblend.activity.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var loginEmail: EditText
    private lateinit var loginPassword: EditText
    private lateinit var signupRedirectText: TextView
    private lateinit var loginButton: TextView
    private lateinit var auth: FirebaseAuth

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginEmail = findViewById(R.id.login_email)
        loginPassword = findViewById(R.id.login_password)
        loginButton = findViewById(R.id.textView_buttonLogin)
        signupRedirectText = findViewById(R.id.signUpRedirectText)
        auth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            val email = loginEmail.text.toString().trim()
            val pass = loginPassword.text.toString().trim()

            if (validateInputs(email, pass)) {
                signInUser(email, pass)
            }
        }

        signupRedirectText.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        userViewModel.firebaseUser.observe(this, Observer { user ->
            if (user != null) {
                welcomeDialog()
            }
        })
    }

    private fun welcomeDialog() {
        AlertDialog.Builder(this)
            .setTitle("¡Bienvenido a Burger Blend!")
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .show()
    }

    private fun validateInputs(email: String, pass: String): Boolean {
        return when {
            email.isEmpty() -> {
                loginEmail.error = "Requiere email"
                false
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                loginEmail.error = "Por favor introduzca una dirección de correo electrónico válida"
                false
            }

            pass.isEmpty() -> {
                loginPassword.error = "Se requiere contraseña"
                false
            }

            pass.length < 6 -> {
                loginPassword.error = "La contraseña debe tener al menos 6 caracteres"
                false
            }

            else -> true
        }
    }

    private fun signInUser(email: String, pass: String) {
        userViewModel.signIn(email, pass, {
        }, { e ->
            Toast.makeText(this, "Error de inicio de sesión: ${e.message}", Toast.LENGTH_SHORT)
                .show()
        })
    }
}
