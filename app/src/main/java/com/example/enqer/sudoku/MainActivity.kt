package com.example.enqer.sudoku


import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
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
        homeLayout.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val b: Button = findViewById(R.id.button2);
                b.text="ewqeqw"
            }
        })

        // button onclick starting new game
        val newGame: Button = findViewById(R.id.button);
        newGame.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?){
                
            }
        })

    }
}