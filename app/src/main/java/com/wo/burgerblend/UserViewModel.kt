package com.wo.burgerblend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserViewModel: ViewModel() {
    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> get() = _user

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        _user.value = auth.currentUser // Cargar usuario actual al iniciar
    }

    fun signIn(email: String, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _user.value = it.user
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }

    fun signOut() {
        auth.signOut()
        _user.value = null
    }
}
