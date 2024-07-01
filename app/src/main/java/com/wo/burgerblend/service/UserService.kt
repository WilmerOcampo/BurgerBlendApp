package com.wo.burgerblend.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wo.burgerblend.database.DatabaseHelper
import com.wo.burgerblend.domain.User
import kotlinx.coroutines.tasks.await
import java.io.File

class UserService(private val context: Context) {
    private val database = FirebaseDatabase.getInstance()
    private val reference = database.getReference("users")
    private val auth = FirebaseAuth.getInstance()
    private val sqlLite = DatabaseHelper(context)

    fun users(callback: (List<User>) -> Unit) {
        if (isNetworkAvailable()) {
            usersFirebase(callback)
        } else {
            usersSQLite(callback)
        }
    }

    private fun usersFirebase(callback: (List<User>) -> Unit) {
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
                //saveUsersToSQLite(users)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
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

    suspend fun createUser(user: User) {
        val credential = auth.createUserWithEmailAndPassword(user.email, user.password).await()
        user.uid = credential.user?.uid.toString()
        reference.push().setValue(user)

    }

    suspend fun updateUser(user: User) {
        reference.child(user.key).setValue(user)
    }

    suspend fun save(user: User, file: File) {
        if (user.key != null) {
            updateUser(user)
        } else {
            createUser(user)
        }
    }

    suspend fun deleteUser(user: User) {
        reference.child(user.key).removeValue()
    }

    suspend fun updateActiveState(key: String, active: Boolean) {
        reference.child(key).child("active").setValue(active)
    }

    suspend fun maxId(callback: (Int) -> Unit) {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children.mapNotNull { it.getValue(User::class.java) }
                val maxId = users.maxByOrNull { it.id }?.id ?: 1000
                callback(maxId.toInt())
            }

            override fun onCancelled(error: DatabaseError) {
                callback(1000)
            }
        })
    }

    private fun usersSQLite(callback: (List<User>) -> Unit) {
        //val users = sqlLite.users()
        //callback(users)
    }

    private fun saveUsersToSQLite(users: List<User>) {
        //sqlLite.deleteUsers()
        //sqlLite.saveUsers(users)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val capabilities =
            connectivityManager.getNetworkCapabilities(network)
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }
}
