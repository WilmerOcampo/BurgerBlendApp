package com.wo.burgerblend.helper

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils
import com.google.gson.Gson
import com.wo.burgerblend.domain.Food
import java.util.Arrays

class StorageHelper(appContext: Context?) {
    private val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(appContext)


    // Getters

    /**
     * Get parsed ArrayList of String from SharedPreferences at 'key'
     * @param key SharedPreferences key
     * @return ArrayList of String
     */
    private fun getListString(key: String?): ArrayList<String> {
        return ArrayList(Arrays.asList(*TextUtils.split(preferences.getString(key, ""), "‚‗‚")))
    }


    fun getListObject(key: String?): ArrayList<Food> {
        val gson = Gson()

        val objStrings = getListString(key)
        val playerList = ArrayList<Food>()

        for (jObjString in objStrings) {
            val player = gson.fromJson(jObjString, Food::class.java)
            playerList.add(player)
        }
        return playerList
    }


    // Put methods

    /**
     * Put ArrayList of String into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param stringList ArrayList of String to be added
     */
    private fun putListString(key: String?, stringList: ArrayList<String>) {
        checkForNullKey(key)
        val myStringList = stringList.toTypedArray<String>()
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply()
    }

    /**
     * Guarda 'playerList' como una lista de objetos Food en SharedPreferences con la clave 'key'
     * @param key clave de SharedPreferences
     * @param playerList lista de objetos Food a guardar
     */
    fun putListObject(key: String?, playerList: ArrayList<Food>) {
        checkForNullKey(key)
        val gson = Gson()
        val objStrings = ArrayList<String>()
        for (player in playerList) {
            objStrings.add(gson.toJson(player))
        }
        putListString(key, objStrings)
    }

    /**
     * null keys would corrupt the shared pref file and make them unreadable this is a preventive measure
     * @param key the pref key to check
     */
    private fun checkForNullKey(key: String?) {
        if (key == null) {
            throw NullPointerException()
        }
    }
}
