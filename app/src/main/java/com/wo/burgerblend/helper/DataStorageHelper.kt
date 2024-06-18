package com.wo.burgerblend.helper

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.wo.burgerblend.domain.Food
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Arrays

class DataStorageHelper(context: Context) {
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private var DEFAULT_APP_IMAGEDATA_DIRECTORY: String? = null
    var lastImagePath: String = ""
        private set

    /**
     * Decodifica el Bitmap desde 'path' y lo devuelve
     * @param path ruta de la imagen
     * @return el Bitmap desde 'path'
     */
    fun getImage(path: String): Bitmap? {
        var bitmapFromPath: Bitmap? = null
        try {
            bitmapFromPath = BitmapFactory.decodeFile(path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmapFromPath
    }

    /**
     * Guarda 'theBitmap' en la carpeta 'theFolder' con el nombre 'theImageName'
     * @param theFolder la ruta de la carpeta en la que deseas guardarla, ej. "DropBox/WorkImages"
     * @param theImageName el nombre que deseas asignar al archivo de imagen, ej. "MeAtLunch.png"
     * @param theBitmap la imagen que deseas guardar como Bitmap
     * @return devuelve la ruta completa (dirección del sistema de archivos) de la imagen guardada
     */
    fun putImage(theFolder: String?, theImageName: String?, theBitmap: Bitmap?): String? {
        if (theFolder == null || theImageName == null || theBitmap == null) return null
        DEFAULT_APP_IMAGEDATA_DIRECTORY = theFolder
        val mFullPath = setupFullPath(theImageName)
        if (mFullPath.isNotEmpty()) {
            lastImagePath = mFullPath
            saveBitmap(mFullPath, theBitmap)
        }
        return mFullPath
    }

    /**
     * Guarda 'theBitmap' en 'fullPath'
     * @param fullPath ruta completa del archivo de imagen, ej. "Images/MeAtLunch.png"
     * @param theBitmap la imagen que deseas guardar como Bitmap
     * @return true si la imagen se guardó, false en caso contrario
     */
    fun putImageWithFullPath(fullPath: String?, theBitmap: Bitmap?): Boolean {
        return fullPath != null && theBitmap != null && saveBitmap(fullPath, theBitmap)
    }

    /**
     * Crea la ruta para la imagen con nombre 'imageName' en el directorio DEFAULT_APP..
     * @param imageName nombre de la imagen
     * @return la ruta completa de la imagen. Si falla al crear el directorio, devuelve una cadena vacía
     */
    private fun setupFullPath(imageName: String): String {
        val mFolder = File(Environment.getExternalStorageDirectory(), DEFAULT_APP_IMAGEDATA_DIRECTORY!!)
        if (isExternalStorageReadable() && isExternalStorageWritable() && !mFolder.exists()) {
            if (!mFolder.mkdirs()) {
                Log.e("ERROR", "Failed to setup folder")
                return ""
            }
        }
        return "${mFolder.path}/$imageName"
    }

    /**
     * Guarda el Bitmap como un archivo PNG en la ruta 'fullPath'
     * @param fullPath ruta del archivo de imagen
     * @param bitmap la imagen como Bitmap
     * @return true si se guardó con éxito, false en caso contrario
     */
    private fun saveBitmap(fullPath: String?, bitmap: Bitmap?): Boolean {
        if (fullPath == null || bitmap == null) return false

        var fileCreated = false
        var bitmapCompressed = false
        var streamClosed = false

        val imageFile = File(fullPath)

        if (imageFile.exists()) {
            if (!imageFile.delete()) return false
        }

        try {
            fileCreated = imageFile.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(imageFile)
            bitmapCompressed = bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        } catch (e: Exception) {
            e.printStackTrace()
            bitmapCompressed = false
        } finally {
            if (out != null) {
                try {
                    out.flush()
                    out.close()
                    streamClosed = true
                } catch (e: IOException) {
                    e.printStackTrace()
                    streamClosed = false
                }
            }
        }

        return fileCreated && bitmapCompressed && streamClosed
    }

    // Métodos para obtener valores

    /**
     * Obtiene un valor int de SharedPreferences en 'key'. Si no se encuentra la clave, devuelve 0
     * @param key clave de SharedPreferences
     * @return valor int en 'key' o 0 si no se encuentra la clave
     */
    fun getInt(key: String): Int {
        return preferences.getInt(key, 0)
    }

    /**
     * Obtiene un ArrayList de enteros de SharedPreferences en 'key'
     * @param key clave de SharedPreferences
     * @return ArrayList de enteros
     */
    fun getListInt(key: String): ArrayList<Int> {
        val myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚")
        val arrayToList = ArrayList(Arrays.asList(*myList))
        val newList = ArrayList<Int>()
        for (item in arrayToList) newList.add(item.toInt())
        return newList
    }

    /**
     * Obtiene un valor long de SharedPreferences en 'key'. Si no se encuentra la clave, devuelve 0
     * @param key clave de SharedPreferences
     * @return valor long en 'key' o 0 si no se encuentra la clave
     */
    fun getLong(key: String): Long {
        return preferences.getLong(key, 0)
    }

    /**
     * Obtiene un valor float de SharedPreferences en 'key'. Si no se encuentra la clave, devuelve 0
     * @param key clave de SharedPreferences
     * @return valor float en 'key' o 0 si no se encuentra la clave
     */
    fun getFloat(key: String): Float {
        return preferences.getFloat(key, 0f)
    }

    /**
     * Obtiene un valor double de SharedPreferences en 'key'. Si se lanza una excepción, devuelve 0
     * @param key clave de SharedPreferences
     * @return valor double en 'key' o 0 si se lanza una excepción
     */
    fun getDouble(key: String): Double {
        val number = getString(key)
        return try {
            number.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    /**
     * Obtiene un ArrayList de Double de SharedPreferences en 'key'
     * @param key clave de SharedPreferences
     * @return ArrayList de Double
     */
    fun getListDouble(key: String): ArrayList<Double> {
        val myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚")
        val arrayToList = ArrayList(Arrays.asList(*myList))
        val newList = ArrayList<Double>()
        for (item in arrayToList) newList.add(item.toDouble())
        return newList
    }

    /**
     * Obtiene un ArrayList de Longs de SharedPreferences en 'key'
     * @param key clave de SharedPreferences
     * @return ArrayList de Longs
     */
    fun getListLong(key: String): ArrayList<Long> {
        val myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚")
        val arrayToList = ArrayList(Arrays.asList(*myList))
        val newList = ArrayList<Long>()
        for (item in arrayToList) newList.add(item.toLong())
        return newList
    }

    /**
     * Obtiene un valor String de SharedPreferences en 'key'. Si no se encuentra la clave, devuelve ""
     * @param key clave de SharedPreferences
     * @return valor String en 'key' o "" (cadena vacía) si no se encuentra la clave
     */
    fun getString(key: String): String {
        return preferences.getString(key, "") ?: ""
    }

    /**
     * Obtiene un ArrayList de Strings de SharedPreferences en 'key'
     * @param key clave de SharedPreferences
     * @return ArrayList de Strings
     */
    fun getListString(key: String): ArrayList<String> {
        return ArrayList(Arrays.asList(*TextUtils.split(preferences.getString(key, ""), "‚‗‚")))
    }

    /**
     * Obtiene un valor boolean de SharedPreferences en 'key'. Si no se encuentra la clave, devuelve false
     * @param key clave de SharedPreferences
     * @return valor boolean en 'key' o false si no se encuentra la clave
     */
    fun getBoolean(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

    /**
     * Obtiene un ArrayList de Boolean de SharedPreferences en 'key'
     * @param key clave de SharedPreferences
     * @return ArrayList de Boolean
     */
    fun getListBoolean(key: String): ArrayList<Boolean> {
        val myList = getListString(key)
        val newList = ArrayList<Boolean>()
        for (item in myList) {
            if (item == "true") {
                newList.add(true)
            } else {
                newList.add(false)
            }
        }
        return newList
    }

    /**
     * Obtiene un ArrayList de objetos de SharedPreferences en 'key'. La clase del objeto es 'mClass'
     * @param key clave de SharedPreferences
     * @param mClass clase del objeto
     * @param <T> tipo de clase del objeto
     * @return ArrayList de objetos o NULL si no se encuentra la clave
    </T> */
    fun <T> getListObject(key: String, mClass: Class<Array<T>>): ArrayList<T>? {
        val gson = Gson()
        val objStrings = getListString(key)
        val objects = ArrayList<T>()
        for (jObjString in objStrings) {
            val value = gson.fromJson(jObjString, mClass)
            objects.addAll(value)
        }
        return objects
    }

    /**
     * Obtiene un objeto de SharedPreferences en 'key'. La clase del objeto es 'classOfT'
     * @param key clave de SharedPreferences
     * @param classOfT clase del objeto
     * @param <T> tipo de clase del objeto
     * @return el objeto o NULL si no se encuentra la clave
    </T> */
    fun <T> getObject(key: String, classOfT: Class<T>): T? {
        val json = getString(key)
        return Gson().fromJson(json, classOfT)
    }

    // Métodos para guardar valores

    /**
     * Guarda un valor int en SharedPreferences con 'key'
     * @param key clave de SharedPreferences
     * @param value valor int que deseas guardar
     */
    fun putInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    /**
     * Guarda un ArrayList de enteros en SharedPreferences con 'key'
     * @param key clave de SharedPreferences
     * @param intList ArrayList de enteros que deseas guardar
     */
    fun putListInt(key: String, intList: ArrayList<Int>) {
        val myIntList = arrayOfNulls<String>(intList.size)
        for (i in intList.indices) {
            myIntList[i] = intList[i].toString()
        }
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myIntList)).apply()
    }

    /**
     * Guarda un valor long en SharedPreferences con 'key'
     * @param key clave de SharedPreferences
     * @param value valor long que deseas guardar
     */
    fun putLong(key: String, value: Long) {
        preferences.edit().putLong(key, value).apply()
    }

    /**
     * Guarda un valor float en SharedPreferences con 'key'
     * @param key clave de SharedPreferences
     * @param value valor float que deseas guardar
     */
    fun putFloat(key: String, value: Float) {
        preferences.edit().putFloat(key, value).apply()
    }

    /**
     * Guarda un valor double en SharedPreferences con 'key'
     * @param key clave de SharedPreferences
     * @param value valor double que deseas guardar
     */
    fun putDouble(key: String, value: Double) {
        putString(key, value.toString())
    }

    /**
     * Guarda un ArrayList de Double en SharedPreferences con 'key'
     * @param key clave de SharedPreferences
     * @param doubleList ArrayList de Double que deseas guardar
     */
    fun putListDouble(key: String, doubleList: ArrayList<Double>) {
        val myDoubleList = arrayOfNulls<String>(doubleList.size)
        for (i in doubleList.indices) {
            myDoubleList[i] = doubleList[i].toString()
        }
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myDoubleList)).apply()
    }

    /**
     * Guarda un ArrayList de Long en SharedPreferences con 'key'
     * @param key clave de SharedPreferences
     * @param longList ArrayList de Long que deseas guardar
     */
    fun putListLong(key: String, longList: ArrayList<Long>) {
        val myLongList = arrayOfNulls<String>(longList.size)
        for (i in longList.indices) {
            myLongList[i] = longList[i].toString()
        }
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myLongList)).apply()
    }

    /**
     * Guarda un valor String en SharedPreferences con 'key'
     * @param key clave de SharedPreferences
     * @param value valor String que deseas guardar
     */
    fun putString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    /**
     * Guarda un ArrayList de Strings en SharedPreferences con 'key'
     * @param key clave de SharedPreferences
     * @param stringList ArrayList de Strings que deseas guardar
     */
    fun putListString(key: String, stringList: ArrayList<String>) {
        preferences.edit().putString(key, TextUtils.join("‚‗‚", stringList)).apply()
    }

    /**
     * Guarda un valor boolean en SharedPreferences con 'key'
     * @param key clave de SharedPreferences
     * @param value valor boolean que deseas guardar
     */
    fun putBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    /**
     * Guarda un ArrayList de Boolean en SharedPreferences con 'key'
     * @param key clave de SharedPreferences
     * @param boolList ArrayList de Boolean que deseas guardar
     */
    fun putListBoolean(key: String, boolList: ArrayList<Boolean>) {
        val newList = ArrayList<String>()
        for (item in boolList) {
            if (item) {
                newList.add("true")
            } else {
                newList.add("false")
            }
        }
        putListString(key, newList)
    }

    /**
     * Guarda un ArrayList de objetos en SharedPreferences con 'key'
     * @param key clave de SharedPreferences
     * @param objArray ArrayList de objetos que deseas guardar
     */
    fun putListObject(key: String, objArray: ArrayList<Food>) {
        val gson = Gson()
        val objStrings = ArrayList<String>()
        for (obj in objArray) {
            objStrings.add(gson.toJson(obj))
        }
        putListString(key, objStrings)
    }

    /**
     * Guarda un objeto en SharedPreferences con 'key'
     * @param key clave de SharedPreferences
     * @param obj objeto que deseas guardar
     */
    fun putObject(key: String, obj: Any) {
        val gson = Gson()
        putString(key, gson.toJson(obj))
    }

    /**
     * Elimina un elemento de SharedPreferences con 'key'
     * @param key clave de SharedPreferences
     */
    fun remove(key: String) {
        preferences.edit().remove(key).apply()
    }

    /**
     * Elimina el archivo de imagen en 'path'
     * @param path ruta del archivo de imagen
     * @return true si se eliminó con éxito, false en caso contrario
     */
    fun deleteImage(path: String): Boolean {
        val file = File(path)
        return file.delete()
    }

    /**
     * Limpia SharedPreferences (elimina todo)
     */
    fun clear() {
        preferences.edit().clear().apply()
    }

    /**
     * Verifica si el almacenamiento externo es escribible
     * @return true si es escribible, false en caso contrario
     */
    private fun isExternalStorageWritable(): Boolean {
        return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
    }

    /**
     * Verifica si el almacenamiento externo es legible
     * @return true si es legible, false en caso contrario
     */
    private fun isExternalStorageReadable(): Boolean {
        return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || Environment.MEDIA_MOUNTED_READ_ONLY == Environment.getExternalStorageState()
    }
}