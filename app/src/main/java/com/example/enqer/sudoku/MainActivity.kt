package com.example.enqer.sudoku




import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // SheredPreference dark mode
        val appSettingPref: SharedPreferences = getSharedPreferences("AppSettingPref", 0)
        val sharedPrefsEdit: SharedPreferences.Editor = appSettingPref.edit()
        val isNightMode: Boolean = appSettingPref.getBoolean("NightMode", false)

        if(isNightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }



        // button home onclick changing layout
        val homeLayout = findViewById<View>(R.id.home) as LinearLayout
        homeLayout.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val b: Button = findViewById(R.id.button);
                b.text = "ewqeqw"
            }
        })

        // button home onclick changing activity
        val statsLayout = findViewById<View>(R.id.stats) as LinearLayout
        statsLayout.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val b: Button = findViewById(R.id.button2);
                b.text = "ewqeqw"
            }
        })

        // button onclick starting new game
        val newGame: Button = findViewById(R.id.button);
        newGame.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

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
                    diff = when (item.title){
                        "Łatwy"  -> "Łatwy"
                        "Średni" -> "Średni"
                        "Trudny" -> "Trudny"
                        else -> "Łatwy"
                    }
                    // changing activity
                    val intent: Intent = Intent(this@MainActivity, GameActivity::class.java)
                    intent.putExtra("diff", diff)
                    startActivity(intent)

                    true
                }
                popup.show()


            }
        })

    }
}
