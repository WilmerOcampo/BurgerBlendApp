package com.wo.burgerblend.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.wo.burgerblend.R

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var signupFirstName: EditText
    private lateinit var signupLastName: EditText
    private lateinit var signupEmail: EditText
    private lateinit var signupAddress: EditText
    private lateinit var signupPhone: EditText
    private lateinit var signupPassword: EditText
    private lateinit var signupConfirmPassword: EditText
    private lateinit var signupButton: Button
    private lateinit var loginRedirectText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        signupFirstName = findViewById(R.id.signup_first_name)
        signupLastName = findViewById(R.id.signup_last_name)
        signupEmail = findViewById(R.id.signup_email)
        signupAddress = findViewById(R.id.signup_address)
        signupPhone = findViewById(R.id.signup_phone)
        signupPassword = findViewById(R.id.signup_password)
        signupConfirmPassword = findViewById(R.id.signup_confirm_password)
        signupButton = findViewById(R.id.signup_button)
        loginRedirectText = findViewById(R.id.loginRedirectText)

        signupButton.setOnClickListener {
            val firstName = signupFirstName.text.toString().trim()
            val lastName = signupLastName.text.toString().trim()
            val email = signupEmail.text.toString().trim()
            val address = signupAddress.text.toString().trim()
            val phone = signupPhone.text.toString().trim()
            val password = signupPassword.text.toString().trim()
            val confirmPassword = signupConfirmPassword.text.toString().trim()

            if (firstName.isEmpty()) {
                signupFirstName.error = "El nombre no puede estar vacío"
                return@setOnClickListener
            }
            if (lastName.isEmpty()) {
                signupLastName.error = "El apellido no puede estar vacío"
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                signupEmail.error = "El correo no puede estar vacío"
                return@setOnClickListener
            }
            if (address.isEmpty()) {
                signupAddress.error = "La dirección no puede estar vacía"
                return@setOnClickListener
            }
            if (phone.isEmpty()) {
                signupPhone.error = "El celular no puede estar vacío"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                signupPassword.error = "La contraseña no puede estar vacía"
                return@setOnClickListener
            }
            if (confirmPassword.isEmpty()) {
                signupConfirmPassword.error = "Confirmar contraseña no puede estar vacío"
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                signupConfirmPassword.error = "Las contraseñas no coinciden"
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Registro exitoso",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                } else {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Registro fallido: " + (task.exception?.message ?: "Error desconocido"),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        loginRedirectText.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
        }
    }
}
