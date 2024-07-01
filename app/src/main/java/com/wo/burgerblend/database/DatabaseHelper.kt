package com.wo.burgerblend.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.wo.burgerblend.domain.Category

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "BurgerBlendDatabase.db"
        private const val TABLE_CATEGORIES = "categories"
        private const val TABLE_FOODS = "foods"
        private const val KEY_CATEGORY = "_key"
        private const val KEY_CATEGORY_ID = "id"
        private const val KEY_CATEGORY_NAME = "name"
        private const val KEY_CATEGORY_DESCRIPTION = "description"
        private const val KEY_CATEGORY_IMAGE = "image"
        // Otros campos de la tabla
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createCategoriesTable =
            "CREATE TABLE $TABLE_CATEGORIES ($KEY_CATEGORY TEXT, $KEY_CATEGORY_ID INTEGER PRIMARY KEY, $KEY_CATEGORY_NAME TEXT, $KEY_CATEGORY_DESCRIPTION TEXT, $KEY_CATEGORY_IMAGE TEXT)"
        db?.execSQL(createCategoriesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Aquí puedes implementar la lógica de actualización de la base de datos si cambia la versión
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
        onCreate(db)
    }

    fun deleteCategories() {
        val db = writableDatabase
        db.delete(TABLE_CATEGORIES, null, null)
        db.close()
    }

    fun saveCategories(categories: List<Category>) {
        val db = writableDatabase
        categories.forEach { category ->
            val values = ContentValues().apply {
                put(KEY_CATEGORY, category.key)
                put(KEY_CATEGORY_ID, category.id)
                put(KEY_CATEGORY_NAME, category.name)
                put(KEY_CATEGORY_DESCRIPTION, category.description)
                put(KEY_CATEGORY_IMAGE, category.image)
            }
            db.insert(TABLE_CATEGORIES, null, values)
        }
        db.close()
    }

    fun categories(): List<Category> {
        val categories = mutableListOf<Category>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_CATEGORIES", null)

        if (cursor.moveToFirst()) {
            val id = cursor.getColumnIndex(KEY_CATEGORY_ID)
            val name = cursor.getColumnIndex(KEY_CATEGORY_NAME)
            val description = cursor.getColumnIndex(KEY_CATEGORY_DESCRIPTION)
            val image = cursor.getColumnIndex(KEY_CATEGORY_IMAGE)

            do {
                val category = Category(
                    key = if (id != -1) cursor.getString(id) else "",
                    id = if (id != -1) cursor.getLong(id) else 0,
                    name = if (name != -1) cursor.getString(name) else "",
                    description = if (description != -1) cursor.getString(description) else "",
                    image = if (image != -1) cursor.getString(image) else ""
                )
                categories.add(category)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return categories
    }
}
