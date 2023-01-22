package pt.ismai.ac.final2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FavoriteDrinksDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "favorite_drinks.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "favorite_drinks"
        private const val COLUMN_ID = "id"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Create the table
        val createTable = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY);"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades
    }

    fun addDrink(id: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_ID, id)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun removeDrink(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun getAllIDs(): List<Int> {
        val ids = mutableListOf<Int>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(
                COLUMN_ID
            ),
            null,
            null,
            null,
            null,
            null,
            null
        )
        cursor?.moveToFirst()
        while (cursor != null && !cursor.isAfterLast) {
            val id = cursor.getInt(0)
            ids.add(id)
            cursor.moveToNext()
        }
        cursor?.close()
        return ids
    }
}
