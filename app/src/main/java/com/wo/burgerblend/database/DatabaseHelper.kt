package com.wo.burgerblend.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.wo.burgerblend.BurgerBlendApplication
import com.wo.burgerblend.domain.Category
import com.wo.burgerblend.domain.Food

class DatabaseHelper :
    SQLiteOpenHelper(
        BurgerBlendApplication.applicationContext(),
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "BurgerBlendDatabase.db"

        // Tabla de categoríes
        private const val TABLE_CATEGORIES = "categories"
        private const val KEY_CATEGORY = "_key"
        private const val KEY_CATEGORY_ID = "id"
        private const val KEY_CATEGORY_NAME = "name"
        private const val KEY_CATEGORY_DESCRIPTION = "description"
        private const val KEY_CATEGORY_IMAGE = "image"

        // Tabla de foods
        private const val TABLE_FOODS = "foods"
        private const val KEY_FOOD = "_key"
        private const val KEY_FOOD_ID = "id"
        private const val KEY_FOOD_NAME = "name"
        private const val KEY_FOOD_DESCRIPTION = "description"
        private const val KEY_FOOD_CATEGORY = "category"
        private const val KEY_FOOD_PRICE = "price"
        private const val KEY_FOOD_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createCategoriesTable =
            "CREATE TABLE $TABLE_CATEGORIES ($KEY_CATEGORY TEXT, $KEY_CATEGORY_ID INTEGER PRIMARY KEY, $KEY_CATEGORY_NAME TEXT, $KEY_CATEGORY_DESCRIPTION TEXT, $KEY_CATEGORY_IMAGE TEXT)"
        val createFoodsTable =
            "CREATE TABLE $TABLE_FOODS ($KEY_FOOD TEXT, $KEY_FOOD_ID INTEGER PRIMARY KEY, $KEY_FOOD_NAME TEXT, $KEY_FOOD_DESCRIPTION TEXT, $KEY_FOOD_CATEGORY INTEGER, $KEY_FOOD_PRICE REAL, $KEY_FOOD_IMAGE TEXT, FOREIGN KEY($KEY_FOOD_CATEGORY) REFERENCES $TABLE_CATEGORIES($KEY_CATEGORY_ID))"
        db?.execSQL(createCategoriesTable)
        db?.execSQL(createFoodsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Lógica de actualización de la base de datos si cambia la versión
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FOODS")
        onCreate(db)
    }

    // CATEGORIES

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

    // FOODS

    fun deleteFoods() {
        val db = writableDatabase
        db.delete(TABLE_FOODS, null, null)
        db.close()
    }

    fun saveFoods(foods: List<Food>) {
        val db = writableDatabase
        foods.forEach { food ->
            val values = ContentValues().apply {
                put(KEY_FOOD, food.key)
                put(KEY_FOOD_ID, food.id)
                put(KEY_FOOD_NAME, food.name)
                put(KEY_FOOD_DESCRIPTION, food.description)
                put(KEY_FOOD_CATEGORY, food.category)
                put(KEY_FOOD_PRICE, food.price)
                put(KEY_FOOD_IMAGE, food.image)
            }
            db.insert(TABLE_FOODS, null, values)
        }
        db.close()
    }

    fun foods(): List<Food> {
        val foods = mutableListOf<Food>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_FOODS", null)

        if (cursor.moveToFirst()) {
            val key = cursor.getColumnIndex(KEY_FOOD)
            val id = cursor.getColumnIndex(KEY_FOOD_ID)
            val name = cursor.getColumnIndex(KEY_FOOD_NAME)
            val description = cursor.getColumnIndex(KEY_FOOD_DESCRIPTION)
            val category = cursor.getColumnIndex(KEY_FOOD_CATEGORY)
            val price = cursor.getColumnIndex(KEY_FOOD_PRICE)
            val image = cursor.getColumnIndex(KEY_FOOD_IMAGE)

            do {
                val food = Food(
                    key = if (key != -1) cursor.getString(key) else "",
                    id = if (id != -1) cursor.getLong(id) else 0,
                    name = if (name != -1) cursor.getString(name) else "",
                    description = if (description != -1) cursor.getString(description) else "",
                    category = if (category != -1) cursor.getLong(category) else 0,
                    price = if (price != -1) cursor.getDouble(price) else 0.0,
                    image = if (image != -1) cursor.getString(image) else "",
                    quantity = 0
                )
                foods.add(food)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return foods
    }
}
