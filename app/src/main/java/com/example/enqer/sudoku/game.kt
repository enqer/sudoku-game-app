package com.example.enqer.sudoku

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity

class game : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

//        var l = findViewById<androidx.gridlayout.widget.GridLayout>(R.id.tab1)
//        l.setBackgroundColor(R.drawable.border_table)
//        l = findViewById(R.id.dwa)
//        l.setBackgroundColor(R.drawable.border_table)

//        for (i in 1..9){
//            val id: Int = resources.getIdentifier("tab$i", "id", packageName)
//            var l = findViewById<androidx.gridlayout.widget.GridLayout>(id)
//            l.setBackgroundColor(R.drawable.border_table)
//
//        }

    }

    @SuppressLint("ResourceAsColor")
    fun selectField(view: View) {
        val b: Button = findViewById(view.id);
        val tag: String = b.tag.toString()

        for (i in 1..9){
            val id: Int = resources.getIdentifier("${tag.get(0)}$i", "id", packageName)
            val btn: Button = findViewById(id)
            btn.setBackgroundColor(R.color.littleBlue)
        }
        for (i in "abcdefghi"){
            val id: Int = resources.getIdentifier("$i${tag.get(1)}", "id", packageName)
            val btn: Button = findViewById(id)
            btn.setBackgroundColor(R.color.littleBlue)
        }
        b.setBackgroundColor(R.color.pointedBtn)

    }



}