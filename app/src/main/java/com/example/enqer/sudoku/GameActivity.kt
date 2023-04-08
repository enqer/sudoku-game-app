package com.example.enqer.sudoku


import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf


class GameActivity : AppCompatActivity() {
    companion object{

        @SuppressLint("StaticFieldLeak")
        lateinit var pointedBtn: Button // the button whose is selected
        val isPointedBtnInit get() = this::pointedBtn.isInitialized

        // Sudoku object and every data from board
        lateinit var sudoku: Sudoku
        const val sizeOfSudoku: Int = 9
        lateinit var difficulty: String
        var missingNumbers: Int = 40

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        // SheredPreference dark mode
        val appSettingPref: SharedPreferences = getSharedPreferences("AppSettingPref", 0)
        val sharedPrefsEdit: SharedPreferences.Editor = appSettingPref.edit()
        val isNightMode: Boolean = appSettingPref.getBoolean("NightMode", false)

        if(isNightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        val changeMode: ImageButton = findViewById(R.id.changeMode)
        changeMode.setOnClickListener{
            if(isNightMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefsEdit.putBoolean("NightMode",false)
                sharedPrefsEdit.apply()
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefsEdit.putBoolean("NightMode",true)
                sharedPrefsEdit.apply()
            }

        }

        // passing data between activities
        val intent = intent

        difficulty = intent.getStringExtra("diff").toString()
        if (difficulty.equals("Łatwy")){
            missingNumbers = 40
            difficulty = "Łatwa"
        } else if (difficulty.equals("Średni")){
            missingNumbers = 50
            difficulty = "Średnia"
        } else {
            missingNumbers = 60
            difficulty = "Trudna"
        }
        val difficultyBtn: TextView = findViewById(R.id.difficultyBtn)
        difficultyBtn.text = difficulty

        sudoku = Sudoku(sizeOfSudoku, missingNumbers)
        printSudoku()


    }

    // zrobic zabezpieczenie przed kliknięciem numbers pierwsze
//    @SuppressLint("ResourceAsColor")
    fun selectField(view: View) {
            pointedBtn = findViewById(view.id);
            val tag: String = pointedBtn.tag.toString()
            var id: Int
            var btn: Button

            // cleaning the board
            for (i in "abcdefghi") {
                for (j in 1..9) {
                    id = resources.getIdentifier("$i$j", "id", packageName)
                    btn = findViewById(id)
                    btn.setBackgroundResource(R.drawable.border)
                }
            }

            // sets a color to the lines
            for (i in 1..9) {
                id = resources.getIdentifier("${tag[0]}$i", "id", packageName)
                btn = findViewById(id)
                btn.setBackgroundResource(R.drawable.border_marked)
            }
            for (i in "abcdefghi") {
                id = resources.getIdentifier("$i${tag[1]}", "id", packageName)
                btn = findViewById(id)
                btn.setBackgroundResource(R.drawable.border_marked)
            }

//         sets a color to the square
        val signs: String = when (tag[0]) {
            in "abc" -> "abc"
            in "def" -> "def"
            else -> "ghi"
        }
        val nums: String = when (tag[1]) {
            in "123" -> "123"
            in "789" -> "789"
            else -> "456"
        }
        for (i in signs) {
            for (j in nums) {
                id = resources.getIdentifier("$i$j", "id", packageName)
                btn = findViewById(id)
                btn.setBackgroundResource(R.drawable.border_marked)
            }
        }
        // set a color to pointed button
        pointedBtn.setBackgroundResource(R.drawable.border_marked_one)

    }

    fun selectNumber(view: View){
        val btnNumber: Button = findViewById(view.id);
        if (isPointedBtnInit){
            pointedBtn.text = btnNumber.text
        }
//        pointedBtn.text = btnNumber.text
    }

    fun printSudoku(){
        var id: Int
        var btn: Button

        var k = 0
        // cleaning the board
        for (i in "abcdefghi") {
            for (j in 1..9) {
                id = resources.getIdentifier("$i$j", "id", packageName)
                btn = findViewById(id)
                if (sudoku.mat[k][j-1].toString().equals("0")){
                    btn.text = ""
                }else{
                    btn.text= sudoku.mat[k][j-1].toString()
                }

            }
            k++
        }
    }

    // Saving and restoring data
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var btn: Button
        var id: Int
        val data = ArrayList<String>()
        Log.i("SAVE","WORKS")
        for (i in "abcdefghi") {
            for (j in 1..9) {
                id = resources.getIdentifier("$i$j", "id", packageName)
                btn = findViewById(id)
                data.add(btn.text.toString())
            }
        }
        outState.putStringArrayList("data",data)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("RESTORE","WORKS")
        val data = savedInstanceState.getStringArrayList("data") as ArrayList<String>
        var btn: Button
        var id: Int
        Log.i("SAVE","WORKS")
        var index = 0
        for (i in "abcdefghi") {
            for (j in 1..9) {
                id = resources.getIdentifier("$i$j", "id", packageName)
                btn = findViewById(id)
                btn.text = data[index].toString()
                index++
            }
        }
    }




}

