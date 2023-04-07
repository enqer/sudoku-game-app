package com.example.enqer.sudoku


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class GameActivity : AppCompatActivity() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        private lateinit var pointedBtn: Button
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


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
        pointedBtn.setBackgroundColor(R.drawable.border_marked_one)

    }

    fun selectNumber(view: View){
        val btnNumber: Button = findViewById(view.id);
        pointedBtn.text = btnNumber.text
    }



}