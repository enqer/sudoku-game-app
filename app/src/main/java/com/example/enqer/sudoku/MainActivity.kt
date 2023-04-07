package com.example.enqer.sudoku



import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // button home onclick changing layout
        val homeLayout = findViewById<View>(R.id.home) as LinearLayout
        homeLayout.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val b: Button = findViewById(R.id.button);
                b.text="ewqeqw"
            }
        })

        // button home onclick changing activity
        val statsLayout = findViewById<View>(R.id.stats) as LinearLayout
        statsLayout.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val b: Button = findViewById(R.id.button2);
                b.text="ewqeqw"
            }
        })

        // button onclick starting new game
        val newGame: Button = findViewById(R.id.button);
        newGame.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?){
                val popup = PopupMenu(this@MainActivity, newGame)
                //Inflating the Popup using xml file
                //Inflating the Popup using xml file
                popup.menuInflater
                    .inflate(R.menu.menu_game, popup.menu)

                //registering popup with OnMenuItemClickListener

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener { item ->
                    Toast.makeText(
                        this@MainActivity,
                        "You Clicked : " + item.getTitle(),
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }
                popup.show()

//                val intent: Intent = Intent(this@MainActivity, GameActivity::class.java)
//                startActivity(intent)
            }
        })

    }
}