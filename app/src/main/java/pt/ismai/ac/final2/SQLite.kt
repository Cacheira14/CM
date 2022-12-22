package pt.ismai.ac.final2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DrinksContract {
    companion object {
        const val DATABASE_NAME = "drinks_database"
        const val TABLE_NAME = "drinks"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_THUMBNAIL_URL = "thumbnail_url"
    }
}

class DrinksDbHelper(context: Context) : SQLiteOpenHelper(context, DrinksContract.DATABASE_NAME, null, 1) {

    companion object {
        const val DATABASE_NAME = "drinks_database"
        const val TABLE_NAME = "drinks"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_THUMBNAIL_URL = "thumbnail_url"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableSql = "CREATE TABLE ${DrinksContract.TABLE_NAME} (" +
                "${DrinksContract.COLUMN_ID} INTEGER PRIMARY KEY," +
                "${DrinksContract.COLUMN_NAME} TEXT," +
                "${DrinksContract.COLUMN_THUMBNAIL_URL} TEXT" +
                ")"
        db?.execSQL(createTableSql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableSql = "DROP TABLE IF EXISTS ${DrinksContract.TABLE_NAME}"
        db?.execSQL(dropTableSql)
        onCreate(db)
    }

    fun insertDrink(drink: Drink) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(DrinksContract.COLUMN_ID, drink.idDrink)
            put(DrinksContract.COLUMN_NAME, drink.strDrink)
            put(DrinksContract.COLUMN_THUMBNAIL_URL, drink.strDrinkThumb)
        }
        db.insert(DrinksContract.TABLE_NAME, null, values)
        db.close()
    }

    fun deleteDrink(drinkId: String) {
        val db = writableDatabase
        db.delete(DrinksContract.TABLE_NAME, "${DrinksContract.COLUMN_ID} = ?", arrayOf(drinkId.toString()))
        db.close()
    }

    fun getAllDrinks(): List<Drink> {
        val drinks = mutableListOf<Drink>()
        val db = readableDatabase
        val cursor = db.query(DrinksContract.TABLE_NAME, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndexOrThrow(DrinksContract.COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(DrinksContract.COLUMN_NAME))
            val thumbnailUrl = cursor.getString(cursor.getColumnIndexOrThrow(DrinksContract.COLUMN_THUMBNAIL_URL))
            val drink = Drink(id, name, thumbnailUrl)
            drinks.add(drink)
        }
        cursor.close()
        db.close()
        return drinks
    }
}