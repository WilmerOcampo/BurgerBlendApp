package com.wo.burgerblend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.wo.burgerblend.domain.User
import com.wo.burgerblend.service.UserService

class UserViewModel : ViewModel() {
    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    val firebaseUser: LiveData<FirebaseUser?> get() = _firebaseUser

    private val _userDetails = MutableLiveData<User?>()
    val userDetails: LiveData<User?> get() = _userDetails

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userService = UserService()

    init {
        _firebaseUser.value = auth.currentUser
        userDetails()
    }

    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _firebaseUser.value = it.user
                userDetails()
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }

    fun signOut() {
        auth.signOut()
        _firebaseUser.value = null
        _userDetails.value = null
    }

    private fun userDetails() {
        _firebaseUser.value?.uid?.let { uid ->
            userService.userByUid(uid) { user ->
                _userDetails.value = user
            }
        }
    }
}
