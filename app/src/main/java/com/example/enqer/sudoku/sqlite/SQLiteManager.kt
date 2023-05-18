package com.example.enqer.sudoku.sqlite

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
                + ID + " INTEGER PRIMARY KEY, AUTO_INCREMENT,"
                + ISWINNER + " TEXT, "
                + DIFFICULTY + " TEXT,"
                + MISTAKES + " INTEGER,"
                + POINTS + " INTEGER,"
                + TIME + " DOUBLE " + ")")
        db?.execSQL(createTableCashify)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // name = name of table
    fun insertStat(isWinner: String, difficulty: String, mistakes: Int,points: Int, time: Double ): Long{
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

    // returns number of games
    fun getGamesPlayedByDifficulty(diff: String): Int{
        val selectQuery = "SELECT COUNT(*) FROM $TABLE_NAME WHERE $DIFFICULTY='$diff'"
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return 0
        }
        if (cursor.moveToFirst()){
            return cursor.getInt(0)
        }
        return 0
    }
    // returns number of winner/loser game
    fun getGamesByDifficulty(diff: String, iswinner: String): Int{
        val selectQuery = "SELECT COUNT(*) FROM $TABLE_NAME WHERE $DIFFICULTY='$diff' AND $ISWINNER='$iswinner'"
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return 0
        }
        if (cursor.moveToFirst()){
            return cursor.getInt(0)
        }
        return 0
    }
    // returns avg mistakes per game
    fun getAvgMistakes(diff: String): Int{
        val selectQuery = "SELECT SUM($MISTAKES) FROM $TABLE_NAME WHERE $DIFFICULTY='$diff'"
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return 0
        }
        if (cursor.moveToFirst()){
            return cursor.getInt(0)
        }
        return 0
    }

    // returns most points
    fun getMostPoints(diff: String): Int{
        val selectQuery = "SELECT MAX($POINTS) FROM $TABLE_NAME WHERE $DIFFICULTY='$diff'"
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return 0
        }
        if (cursor.moveToFirst()){
            return cursor.getInt(0)
        }
        return 0
    }

    // returns all Points
    fun getAllPoints(diff: String): Int{
        val selectQuery = "SELECT SUM($POINTS) FROM $TABLE_NAME WHERE $DIFFICULTY='$diff'"
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return 0
        }
        if (cursor.moveToFirst()){
            return cursor.getInt(0)
        }
        return 0
    }





    //TODO DELETE TABLE jeśli chcemy wyczyścić statystyki onclick!!!

    fun deleteStats(diff: String): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DIFFICULTY, diff)
        val success = db.delete(TABLE_NAME, "$DIFFICULTY='$diff'", null)
        db.close()
        return success
    }

}