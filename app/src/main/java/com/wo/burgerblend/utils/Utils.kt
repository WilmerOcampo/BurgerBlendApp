package com.wo.burgerblend.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.wo.burgerblend.BurgerBlendApplication

object Utils {
    fun isNetworkAvailable(): Boolean {
        val context = BurgerBlendApplication.applicationContext()
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }
}