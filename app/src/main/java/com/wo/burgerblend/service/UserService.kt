package com.wo.burgerblend.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wo.burgerblend.domain.User


class UserService {
    private val reference = FirebaseDatabase.getInstance().getReference("users")
    private val auth = FirebaseAuth.getInstance()

    private val defImg =
        "https://firebasestorage.googleapis.com/v0/b/burger-blend.appspot.com/o/users%2FUser%20Image%20Defaut.jpg?alt=media&token=79f019bd-6a1e-427a-8ed9-8f0766580b9c"

    private fun users(callback: (List<User>) -> Unit) {
        val users: MutableList<User> = mutableListOf()

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                users.clear()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    user?.let {
                        users.add(it)
                    }
                }
                callback(users)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }

    fun currentUserDetails(callback: (User?) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            userByUid(it.uid, callback)
        } ?: callback(null)
    }

    fun userByUid(uid: String, callback: (User?) -> Unit) {
        reference.orderByChild("uid").equalTo(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.children.firstOrNull()?.getValue(User::class.java)
                    callback(user)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(null)
                }
            })
    }

    private fun defaultUserData(user: User) {
        user.image = defImg
        user.role = "USER"
        user.id = maxId() + 1
    }

    fun createUser(user: User) {
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnSuccessListener { authResult ->
                val firebaseUser = authResult.user
                firebaseUser?.let { u ->
                    user.uid = u.uid
                    defaultUserData(user)
                    val key = reference.push().key.toString()
                    reference.child(key).setValue(user)
                }
            }
            .addOnFailureListener { exception ->
                // Manejar fallos en la creación del usuario
            }
    }

    private fun updateUser(user: User) {
        reference.child(user.key).setValue(user)
    }

    fun deleteUser(user: User) {
        reference.child(user.key).removeValue()
    }

    private fun maxId(): Long {
        var maxId = 1000L
        users { users ->
            if (users.isNotEmpty())
                maxId = users.maxOfOrNull { it.id } ?: 1000L
        }
        return maxId
    }
}
