package com.example.enqer.sudoku


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate


class MainActivity : AppCompatActivity() {

    var createNewGame = true

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
        val previousGame: Button = findViewById(R.id.backToGame)

        if (isPreviousGameOver){
            previousGame.visibility = View.INVISIBLE
        } else {
            previousGame.visibility = View.VISIBLE
        }





        // TODO  dorobić zeby było pokazane ile czasu tam było i jaka trudność albo coś
        previousGame.setOnClickListener{
            createNewGame = false
            val intent: Intent = Intent(this@MainActivity, GameActivity::class.java)
//            intent.putExtra("createNewGame", createNewGame)
            sharedPrefsEdit.putBoolean("createNewGame", createNewGame)
            startActivity(intent)
        }



//        // button home onclick changing layout
//        val homeLayout = findViewById<View>(R.id.home) as LinearLayout
//        homeLayout.setOnClickListener {
//            val b: Button = findViewById(R.id.button);
//            b.text = "ewqeqw"
//        }

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
            // style of popup menu to new game
            val wrapper: Context = ContextThemeWrapper(this@MainActivity, R.style.Style_PopupMenu)

            val popup = PopupMenu(wrapper, newGame)

            popup.menuInflater.inflate(R.menu.menu_game, popup.menu)
            var diff: String = "Łatwy"
            popup.setOnMenuItemClickListener { item ->
                //                    Toast.makeText(
                //                        this@MainActivity,
                //                        "You Clicked : " + item.getTitle(),
                //                        Toast.LENGTH_SHORT
                //                    ).show()
                diff = when (item.title) {
                    "Łatwy" -> "Łatwy"
                    "Średni" -> "Średni"
                    "Trudny" -> "Trudny"
                    else -> "Łatwy"
                }
                createNewGame = true
                // changing activity
                val intent: Intent = Intent(this@MainActivity, GameActivity::class.java)
                intent.putExtra("diff", diff)
//                intent.putExtra("createNewGame", createNewGame)
                sharedPrefsEdit.putBoolean("createNewGame", createNewGame)
                startActivity(intent)

                true
            }
            popup.show()
        }

    }

    override fun onRestart() {
        super.onRestart()
        startActivity(intent)
    }

}
