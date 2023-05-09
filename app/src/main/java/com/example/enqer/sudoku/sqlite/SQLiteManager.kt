package com.example.enqer.sudoku.sqlite


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class SQLiteManager(context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "sudokuDB"
        private const val TABLE_NAME = "table_sudoku"
        private const val DATABASE_VERSION = 1
        private const val ID= "id"
        private const val ISWINNER= "iswinner"
        private const val DIFFICULTY= "difficulty"
        private const val MISTAKES= "mistakes"
        private const val POINTS= "points"
        private const val TIME= "time"
    }


    override fun onCreate(db: SQLiteDatabase?) {
        // creating stats table
        val createTableCashify = ("CREATE TABLE "+ TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTO_INCREMENT,"
                + ISWINNER + " TEXT, "
                + DIFFICULTY + " TEXT,"
                + MISTAKES + " INTEGER,"
                + POINTS + " INTEGER,"
                + TIME + " LONG " + ")")
        db?.execSQL(createTableCashify)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
        // check it later
    }

    // name = name of table
    fun insertStat(isWinner: String, difficulty: String, mistakes: String,points: String, time: Long ): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ISWINNER,isWinner)
        contentValues.put(DIFFICULTY, difficulty)
        contentValues.put(MISTAKES, mistakes)
        contentValues.put(POINTS, points)
        contentValues.put(TIME, time)

        val success = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return success
    }


//    fun sumBalance(name: String): Double{
//        val selectQuery = "SELECT SUM(balance) FROM $name"
//        val db = this.readableDatabase
//        val cursor: Cursor?
//        try {
//            cursor = db.rawQuery(selectQuery, null)
//        } catch (e: Exception){
//            e.printStackTrace()
//            db.execSQL(selectQuery)
//            return 0.0
//        }
//        if (cursor.moveToFirst()){
//            return cursor.getDouble(0)
//        }
//        return 0.0
//    }

//    fun sumBalanceByPerson(nameTable: String, n: String): Double{
//        val selectQuery = "SELECT SUM(balance) FROM $nameTable WHERE name='$n';"
//        val db = this.readableDatabase
//        val cursor: Cursor?
//        try {
//            cursor = db.rawQuery(selectQuery, null)
//        } catch (e: Exception){
//            e.printStackTrace()
//            db.execSQL(selectQuery)
//            return 0.0
//        }
//        if (cursor.moveToFirst()){
//            return cursor.getDouble(0)
//        }
//        return 0.0
//    }

//    fun getPerson(nameTable:String, n: String){
//        val selectQuery = "SELECT * FROM $nameTable WHERE name='$n';"
//    }


//    @SuppressLint("Range")
//    fun getStats(name: String): ArrayList<Person> {
//        val personList: ArrayList<Person> = ArrayList()
//        val selectQuery = "SELECT * FROM $name"
//        val db = this.readableDatabase
//
//        val cursor: Cursor?
//
//        try {
//            cursor = db.rawQuery(selectQuery, null)
//        } catch (e: Exception){
//            e.printStackTrace()
//            db.execSQL(selectQuery)
//            return ArrayList()
//        }
//
//        var id: Int
//        var name: String
//        var date: String
//        var balance: Double
//        var content: String
//        var avatar: String
//
//        if (cursor.moveToFirst()){
//            do{
//                id = cursor.getInt(cursor.getColumnIndex("id"))
//                name = cursor.getString(cursor.getColumnIndex("name"))
//                date = cursor.getString(cursor.getColumnIndex("date"))
//                balance = cursor.getDouble(cursor.getColumnIndex("balance"))
//                content = cursor.getString(cursor.getColumnIndex("content"))
//                avatar = cursor.getString(cursor.getColumnIndex("avatar"))
//
//                val person = Person(id = id, name = name, date = date, balance = balance
//                    , content = content, avatar = avatar)
//                personList.add(person)
//
//            } while (cursor.moveToNext())
//        }
//        return personList
//    }

    //TODO DELETE TABLE jeśli chcemy wyczyścić statystyki
//    fun deletePerson(id: Int, name: String): Int {
//        val db = this.writableDatabase
//
//        val contentValues = ContentValues()
//        contentValues.put(ID, id)
//
//        val success = db.delete(name, "id=$id", null)
//        db.close()
//        return success
//    }
//
//    fun deleteContent(con: String, name: String): Int{
//        val db = this.writableDatabase
//
//        val contentValues = ContentValues()
//        contentValues.put(CONTENT, con)
//
//        val success = db.delete(name, "content='$con'", null)
//        db.close()
//        return success
//    }

}