package com.wo.burgerblend.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.wo.burgerblend.R
import com.wo.burgerblend.activity.MainActivity
class LoginActivity : AppCompatActivity() {
    private lateinit var loginEmail: EditText
    private lateinit var loginPassword: EditText
    private lateinit var signupRedirectText: TextView
    private lateinit var loginButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginEmail = findViewById(R.id.login_email)
        loginPassword = findViewById(R.id.login_password)
        loginButton = findViewById(R.id.login_button)
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
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error de inicio de sesion: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
