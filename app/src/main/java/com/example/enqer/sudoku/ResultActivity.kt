package com.example.enqer.sudoku

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text
import kotlin.math.roundToInt

class ResultActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // full screen
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_result)

        val intent = intent
        val difficulty = intent.getStringExtra("difficulty")
        val mistakes = intent.getIntExtra("mistakes",0)
        val points = intent.getIntExtra("points", 0)
        val time = intent.getDoubleExtra("time", 0.0)

        val difficultyText: TextView = findViewById(R.id.difficultyText)
        val mistakesText: TextView = findViewById(R.id.mistakesText)
        val pointsText: TextView = findViewById(R.id.pointsText)
        val timeText: TextView = findViewById(R.id.timeText)
        val toHomeBtn: Button = findViewById(R.id.toHomeBtn)

        // setting data
        difficultyText.text = difficulty
        mistakesText.text = mistakes.toString()
        pointsText.text = points.toString()
        timeText.text = getTimeStringFromDouble(time)

        toHomeBtn.setOnClickListener {
            // metoda która wpisuje do bazy danych
            finish()
            val changeIntent = Intent(this, MainActivity::class.java)
            startActivity(changeIntent)
        }

    }
    //TODO onbackPressed pewnie trzeba zrobić żeby nic nie robił
    // TODO zapisywanie do bazy danych

    override fun onBackPressed() {} // do nothing!
    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60
        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Int, minutes: Int, seconds: Int): String = String.format("%02d:%02d:%02d", hours, minutes, seconds)

}
