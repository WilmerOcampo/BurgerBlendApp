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
import com.wo.burgerblend.domain.User
import com.wo.burgerblend.service.UserService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    private var userService = UserService(this)

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
            registerUser()
        }

        loginRedirectText.setOnClickListener {
            finish()
        }
    }

    private fun registerUser() {
        val firstName = signupFirstName.text.toString().trim()
        val lastName = signupLastName.text.toString().trim()
        val email = signupEmail.text.toString().trim()
        val address = signupAddress.text.toString().trim()
        val phone = signupPhone.text.toString().trim()
        val password = signupPassword.text.toString().trim()
        val confirmPassword = signupConfirmPassword.text.toString().trim()

        validateInputs(firstName, lastName, email, address, phone, password, confirmPassword)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = User(
                    key = "",
                    uid = "",
                    id = 0L,
                    name = firstName,
                    lastname = lastName,
                    image = "",
                    email = email,
                    address = address,
                    phone = phone,
                    role = "",
                    password = password,
                    active = true
                )
                userService.createUser(user)
                runOnUiThread {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Registro exitoso",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Registro fallido: ${e.message}",

                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun validateInputs(
        firstName: String,
        lastName: String,
        email: String,
        address: String,
        phone: String,
        password: String,
        confirmPassword: String
    ) {
        if (firstName.isEmpty()) {
            signupFirstName.error = "El nombre no puede estar vacío"
            return
        }
        if (lastName.isEmpty()) {
            signupLastName.error = "El apellido no puede estar vacío"
            return
        }
        if (email.isEmpty()) {
            signupEmail.error = "El correo no puede estar vacío"
            return
        }
        if (address.isEmpty()) {
            signupAddress.error = "La dirección no puede estar vacía"
            return
        }
        if (phone.isEmpty()) {
            signupPhone.error = "El celular no puede estar vacío"
            return
        }
        if (password.isEmpty()) {
            signupPassword.error = "La contraseña no puede estar vacía"
            return
        }
        if (password.length < 6) {
            signupPassword.error = "La contraseña debe tener al menos 6 caracteres"
            return
        }
        if (confirmPassword.length < 6) {
            signupConfirmPassword.error = "La contraseña debe tener al menos 6 caracteres"
            return
        }
        if (confirmPassword.isEmpty()) {
            signupConfirmPassword.error = "Confirmar contraseña no puede estar vacío"
            return
        }
        if (password != confirmPassword) {
            signupConfirmPassword.error = "Las contraseñas no coinciden"
            return
        }
    }
}
