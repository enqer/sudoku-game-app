package com.example.enqer.sudoku


import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate


class GameActivity : AppCompatActivity() {
    companion object{

        @SuppressLint("StaticFieldLeak")
        lateinit var pointedBtn: Button // the button whose is selected
        val isPointedBtnInit get() = this::pointedBtn.isInitialized


    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // passing data between activities
        val intent = intent
        Log.d("test", intent.getStringExtra("diff").toString())

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val b: Button = findViewById(R.id.a1)
        outState.putString("data", b.text.toString())

    }

    protected override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState);
        val b: Button = findViewById(R.id.a1)
        b.text=savedInstanceState.getString("data")
    }




}

