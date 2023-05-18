package com.example.enqer.sudoku


import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.enqer.sudoku.interfaces.TimeFormatter


class MainActivity : AppCompatActivity() {

    var createNewGame = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // SheredPreference dark mode
        val appSettingPref: SharedPreferences = getSharedPreferences("AppSettingPref", 0)
        val sharedPrefsEdit: SharedPreferences.Editor = appSettingPref.edit()
        val isNightMode: Boolean = appSettingPref.getBoolean("NightMode", false)
        val isPreviousGameOver: Boolean = appSettingPref.getBoolean("isgameover", true)
        Log.i("POWROT", isPreviousGameOver.toString())


        if(isNightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // previous game button
        val previousGame: LinearLayout = findViewById(R.id.backToGame)

        if (isPreviousGameOver){
            previousGame.visibility = View.INVISIBLE
        } else {
            previousGame.visibility = View.VISIBLE
            val timePrevGame = appSettingPref.getLong("time", 0)
            val difPrevGame = appSettingPref.getString("difficulty", "Łatwa")
            val infoBackGame: TextView = findViewById(R.id.infoBackGame)
            infoBackGame.text = "$difPrevGame ∙ ${TimeFormatter.getTimeStringFromDouble(timePrevGame.toDouble())}"
        }



        previousGame.setOnClickListener{
            createNewGame = false
            val intent: Intent = Intent(this@MainActivity, GameActivity::class.java)
            intent.putExtra("createNewGame", createNewGame)
//            sharedPrefsEdit.putBoolean("createNewGame", createNewGame)
            startActivity(intent)
        }


        // button home onclick changing activity
        val statsLayout = findViewById<View>(R.id.stats) as LinearLayout
        statsLayout.setOnClickListener {
//            val b: Button = findViewById(R.id.backToGame);
//            b.text = "ewqeqw"
            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
        }


        // button onclick starting new game
        val newGame: Button = findViewById(R.id.button);
        newGame.setOnClickListener {
            val dialog: Dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.menu_new_game)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
            val window: Window = dialog.window!!
            val wlp = window.attributes
            wlp.gravity = Gravity.BOTTOM
            wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
            window.attributes = wlp
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            window.setDimAmount(0.3f)
            var choice: String = "Łatwy"
            val easyGame: TextView = dialog.findViewById(R.id.newGameEasy)
            val mediumGame: TextView = dialog.findViewById(R.id.newGameMedium)
            val hardGame: TextView = dialog.findViewById(R.id.newGameHard)

            easyGame.setOnClickListener {
                choice = "Łatwy"
                createNewGame = true
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                dialog.dismiss()
                createGame(choice)
                sharedPrefsEdit.putBoolean("createNewGame", createNewGame)
            }
            mediumGame.setOnClickListener {
                choice = "Średni"
                createNewGame = true
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                dialog.dismiss()
                sharedPrefsEdit.putBoolean("createNewGame", createNewGame)
                createGame(choice)
            }
            hardGame.setOnClickListener {
                choice = "Trudny"
                createNewGame = true
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                dialog.dismiss()
                sharedPrefsEdit.putBoolean("createNewGame", createNewGame)
                createGame(choice)
            }
            dialog.create()
            dialog.show()
            dialog.setCanceledOnTouchOutside(true);
        }

    }

    private fun createGame(choice: String) {
        val intent: Intent = Intent(this@MainActivity, GameActivity::class.java)
        intent.putExtra("diff", choice)
        startActivity(intent)
    }

    override fun onRestart() {
        super.onRestart()
        startActivity(intent)
    }

}
