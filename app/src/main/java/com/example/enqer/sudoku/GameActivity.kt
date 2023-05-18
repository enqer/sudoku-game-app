package com.example.enqer.sudoku

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.*
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.enqer.sudoku.databinding.ActivityMainBinding
import com.example.enqer.sudoku.interfaces.TimeFormatter
import com.example.enqer.sudoku.sqlite.SQLiteManager
import com.google.gson.Gson
import kotlin.math.roundToInt


class GameActivity : AppCompatActivity() {
    companion object{

//        var isNewGame = true

        @SuppressLint("StaticFieldLeak")
        lateinit var pointedBtn: Button // the button which is selected
        // coords of pointedBtn
        var x: Int = 0;
        var y: Int = 0;
        val isPointedBtnInit get() = this::pointedBtn.isInitialized
        const val coords = "abcdefghi" // coords of board (from top to bottom)
        @SuppressLint("StaticFieldLeak")
        lateinit var timerTextView: TextView

        // Sudoku object and every data from board
        lateinit var sudoku: Sudoku
        const val sizeOfSudoku: Int = 9
        lateinit var difficulty: String
        var missingNumbers: Int = 40
        var mistakes: Int = 0
        var points: Int = 0
        var hints: Int = 1
        var iteratorPoints: Int = 1
        lateinit var  countNumbers : Array<Int> // count which number is full on board
        var isNotesMode = false


        // checking if the game is over, if not so we can back to previous game
        var isGameOver = false
        var timeTEST = 0

    }
    private lateinit var sqLiteManager: SQLiteManager
    private var isWinner = false

    // timer stuff
    private lateinit var binding: ActivityMainBinding
    private var timerStarted = false
    private lateinit var  serviceIntent: Intent
    private var time = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        sqLiteManager = SQLiteManager(this)
//        isGameOver = false
        Log.i("TEST///", "TETST")
//        savedInstanceState.

//        if (savedInstanceState != null){
//            finish();
//            overridePendingTransition(0, 0);
//            startActivity(intent);
//            overridePendingTransition(0, 0);
//        }
        // time service
        timerTextView= findViewById(R.id.timer)
        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))


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
        var isNewGame = intent.getBooleanExtra("createNewGame", true)
//        val isNewGame = appSettingPref.getBoolean("createNewGame", true)



        if (isNewGame){
            createNewGame(intent)
            sharedPrefsEdit.putBoolean("createNewGame", false)
        } else{
            recreatePreviousGame()
        }



        // Back to Home button
        val backToHomeBtn: ImageButton = findViewById(R.id.backToHome)
        backToHomeBtn.setOnClickListener{
            saveTheData()
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // TODO time crashing??

        // clearing the field
        val clear: LinearLayout = findViewById(R.id.clear)
        clear.setOnClickListener {
            if (isPointedBtnInit && sudoku.mat[x][y] != sudoku.fullMat[x][y]) {
                pointedBtn.text = ""
            }
        }

        val notes: LinearLayout = findViewById(R.id.notes)
        notes.setOnClickListener{
            isNotesMode = !isNotesMode  // true - is in notes mode
            for (i in 1..9){
                val id = resources.getIdentifier("num$i", "id", packageName)
                val btn: Button = findViewById(id)
                if (isNotesMode) btn.setTextColor(ContextCompat.getColor(applicationContext, R.color.grey))
                else btn.setTextColor(ContextCompat.getColor(applicationContext, R.color.granat))
            }
        }

        // getting hint
        val hint: LinearLayout = findViewById(R.id.hint)
        hint.setOnClickListener {
            if (isPointedBtnInit && hints == 1 && sudoku.mat[x][y] != sudoku.fullMat[x][y]) {
                pointedBtn.text = sudoku.fullMat[x][y].toString()
                // changing border
                pointedBtn.setBackgroundResource(R.drawable.border_marked_hint)
                pointedBtn.setTextColor(ContextCompat.getColor(applicationContext, R.color.littleBlack))
                pointedBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22F)
                sudoku.mat[x][y] = sudoku.fullMat[x][y]

                //changing img of hint
                val hintImg: ImageView = findViewById(R.id.hintImg)
                hintImg.setImageResource(R.drawable.ic_light_0)
                // only one hint so off the button
                hints--
                hint.isClickable = false

                // Checking if number is printed 9 times and then disappear
                countNumbers[sudoku.mat[x][y] - 1]++
                if (countNumbers[sudoku.mat[x][y] - 1] == 9) {
                    val id = resources.getIdentifier("num${sudoku.mat[x][y]}", "id", packageName)
                    val btn: Button = findViewById(id)
                    btn.text = ""
                    btn.isClickable = false
                }
            }
        }
    }

    // selecting the fields to set a colors
    fun selectField(view: View) {
            pointedBtn = findViewById(view.id);
            val tag: String = pointedBtn.tag.toString()
            x = coords.indexOf(tag[0])
            y = tag[1].toString().toInt() - 1
            var id: Int
            var btn: Button

            // cleaning the board
            for (i in coords) {
                for (j in 1..9) {
                    id = resources.getIdentifier("$i$j", "id", packageName)
                    btn = findViewById(id)
                    if (pointedBtn.text == btn.text && btn.text != ""){
                        btn.setBackgroundResource(R.drawable.border_marked_one)
                    }else{
                        btn.setBackgroundResource(R.drawable.border)
                    }
                }
            }

            // sets a color to the lines
            for (i in 1..9) {
                id = resources.getIdentifier("${tag[0]}$i", "id", packageName)
                btn = findViewById(id)
                btn.setBackgroundResource(R.drawable.border_marked)
            }
            for (i in coords) {
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
        if (isPointedBtnInit){  // if button is initialized then we can do some things
            if (isNotesMode && sudoku.mat[x][y].toString() != pointedBtn.text.toString()){
                // Only when the hasnt good value
                val notes = Array<String>(9){" "}
                var index = 0
//                   val pointedText = pointedBtn.text.toString()
                for (i in "123456789"){
                    if (i.toString() in pointedBtn.text.toString()){
                        notes[index++] = i.toString()
                    } else {
                        notes[index++] = " "
                    }
                }
                // adding new value to notes of field
                val addToNotes = btnNumber.text.toString()
                if (addToNotes == notes[addToNotes.toInt()-1]){
                    notes[addToNotes.toInt()-1] = " "
                } else {
                    notes[addToNotes.toInt()-1] = addToNotes
                }
//               }
                // notes has a different styles
                pointedBtn.setTextColor(ContextCompat.getColor(applicationContext, R.color.grey))
                pointedBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9F)
                var notesBtn = ""
                // print the note in the field
                for ((counter, i) in notes.withIndex()){
                    notesBtn += "$i "
                    if (counter == 2 || counter == 5){
                        notesBtn += "\n"
                    }
                }
                pointedBtn.text = notesBtn
//               }
            } else {
                if (sudoku.fullMat[x][y] == btnNumber.text.toString().toInt() && sudoku.fullMat[x][y] != sudoku.mat[x][y]) {
                    pointedBtn.text = btnNumber.text
                    points += (1..9).random() * iteratorPoints
                    iteratorPoints += 2
                    val pointsView: TextView = findViewById(R.id.points)
                    pointsView.text = points.toString()
                    pointedBtn.setTextColor(ContextCompat.getColor(applicationContext, R.color.littleBlack))

                    // adding colors to other same numbers of the actually put number
                    var id: Int
                    var btn: Button
                    for (i in coords) {
                        for (j in 1..9) {
                            id = resources.getIdentifier("$i$j", "id", packageName)
                            btn = findViewById(id)
                            if (pointedBtn.text == btn.text && btn.text != ""){
                                btn.setBackgroundResource(R.drawable.border_marked_one)
                            }
                        }
                    }
                    pointedBtn.setBackgroundResource(R.drawable.border_marked_one)
                    sudoku.mat[x][y] = pointedBtn.text.toString().toInt()

                    // Checking if number is printed 9 times and then disappear
                    countNumbers[sudoku.mat[x][y] - 1]++
                    Log.i("ARRAY", countNumbers[sudoku.mat[x][y] - 1].toString())
                    if (countNumbers[sudoku.mat[x][y] - 1] == 9) {
                        btnNumber.text = ""
                        btnNumber.isClickable = false
                    }
                    if (sudoku.fullMat.contentDeepEquals(sudoku.mat)) {
                        Log.i("WINNER", "Smiga")
                        isGameOver = true
                        isWinner = true
                        showResults()
                    }
                } else {
                    if (sudoku.mat[x][y] != sudoku.fullMat[x][y]) {
                        pointedBtn.text = btnNumber.text
                        mistakes++
                        val miss: TextView = findViewById(R.id.mistakes)
                        miss.text = "$mistakes/3"
                        pointedBtn.setTextColor(ContextCompat.getColor(applicationContext, R.color.mistake))
                        if (mistakes == 3) {
                            isGameOver = true
                            isWinner = false
                            showResults()
                            Log.i("LOSER", "Smiga")
                        }
                    }
                }
                pointedBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22F)
            }
        }
    }

    // printing and cleaning the board
    private fun printSudoku(){
        var id: Int
        var btn: Button
        var k = 0
        for (i in coords) {
            for (j in 1..9) {
                id = resources.getIdentifier("$i$j", "id", packageName)
                btn = findViewById(id)
                if (sudoku.mat[k][j-1].toString() == "0"){
                    btn.text = ""
                }else{
                    btn.text= sudoku.mat[k][j-1].toString()
                    countNumbers[sudoku.mat[k][j-1]-1]++
                    if (countNumbers[sudoku.mat[k][j-1]-1] == 9){
                        id = resources.getIdentifier("num${btn.text}", "id", packageName)
                        btn = findViewById(id)
                        btn.text = ""
                        btn.isClickable = false
                    }
                }
            }
            k++
        }
    }

    private fun showResults(){
        val dialog: Dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_result)
        val diff = when (difficulty){
            resources.getString(R.string.easy) -> "easy"
            resources.getString(R.string.medium) -> "medium"
            resources.getString(R.string.hard) -> "hard"
            else -> "unknown"
        }

        val difficultyText: TextView = dialog.findViewById(R.id.difficultyText)
        val mistakesText: TextView = dialog.findViewById(R.id.mistakesText)
        val pointsText: TextView = dialog.findViewById(R.id.pointsText)
        val timeText: TextView = dialog.findViewById(R.id.timeText)
        val winner: TextView = dialog.findViewById(R.id.winner)
        val toHomeBtn: Button = dialog.findViewById(R.id.toHomeBtn)
        difficultyText.text = difficulty
        mistakesText.text = mistakes.toString()
        pointsText.text = points.toString()
        timeText.text = timerTextView.text
        Log.d("timererere", time.toString())
        if (isWinner){
            winner.text = resources.getString(R.string.winner)
            sqLiteManager.insertStat("winner", diff, mistakes, points, time)
        }
        else{
            winner.text = resources.getString(R.string.loser)
            sqLiteManager.insertStat("loser", diff, mistakes, points, time)
        }
        toHomeBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.setOnDismissListener {
            finish()
            val changeIntent = Intent(this, MainActivity::class.java)

            startActivity(changeIntent)
        }
    }


    // Saving instance of data
    override fun onSaveInstanceState(outState: Bundle) {
        saveTheData()
        super.onSaveInstanceState(outState)
    }

    // Restoring instance of data
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        recreatePreviousGame()
        Log.i("RESTORE","WORKS")
//        startActivity(intent)

    }

    // maybe to delete maybe use to some new function
    private fun startStopTimer(){
        if(timerStarted)
            stopTimer()
        else
            startTimer()
    }

    private fun stopTimer(){
        stopService(serviceIntent)
        timerStarted = false
    }

    private fun startTimer(){
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        startService(serviceIntent)
        timerStarted = true

    }

    private val updateTime: BroadcastReceiver = object  : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent) {
            time = p1.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
//            val timerTextView: TextView = findViewById(R.id.timer)
            timerTextView.text = TimeFormatter.getTimeStringFromDouble(time)
        }
    }

//    private fun getTimeStringFromDouble(time: Double): String {
//        val resultInt = time.roundToInt()
//        val hours = resultInt % 86400 / 3600
//        val minutes = resultInt % 86400 % 3600 / 60
//        val seconds = resultInt % 86400 % 3600 % 60
//        return makeTimeString(hours, minutes, seconds)
//    }
//
//    private fun makeTimeString(hours: Int, minutes: Int, seconds: Int): String = String.format("%02d:%02d:%02d", hours, minutes, seconds)


    // creating new game
    private fun createNewGame(intent: Intent){
        // setting difficulty of the game
        difficulty = intent.getStringExtra("diff").toString()
        when (difficulty) {
            "Łatwy" -> {
                missingNumbers = 40
                difficulty = "Łatwa"
            }
            "Średni" -> {
                missingNumbers = 50
                difficulty = "Średnia"
            }
            else -> {
                missingNumbers = 60
                difficulty = "Trudna"
            }
        }
        val difficultyBtn: TextView = findViewById(R.id.difficultyBtn)
        difficultyBtn.text = difficulty

        // setting property of the game
        mistakes = 0
        points = 0
        iteratorPoints = 1
        hints = 1
        countNumbers = Array<Int>(9){0}
        timeTEST = 0

        startStopTimer()
        // creating object of the game
        sudoku = Sudoku(sizeOfSudoku, missingNumbers)

        Log.i("Jeszcze ","raz")

        // printing the board
        printSudoku()

    }

    // creating previous saved game
    private fun recreatePreviousGame(){
        Log.i("recreated", "jeden")
        val sp: SharedPreferences = getSharedPreferences("AppSettingPref", 0)

        difficulty = sp.getString("difficulty", "Łatwa").toString()
        mistakes = sp.getInt("mistakes", 0)
        points = sp.getInt("points", 0)
        iteratorPoints = sp.getInt("iteratorPoints", 1)
        hints = sp.getInt("hints", 1)
        time = sp.getLong("time", 0).toDouble()
        timerStarted = sp.getBoolean("timeStarted", false)

        startStopTimer()
        Log.d("timer", timerStarted.toString())

        Log.i("recreated", "dwa")
        val gson = Gson()
        val json = sp.getString("sudoku","")

        // without it could be crash at next line ( sudoku must be initialized)
        sudoku = Sudoku(sizeOfSudoku, missingNumbers)

        sudoku = if (json != ""){
            gson.fromJson(json, sudoku::class.java)
        } else {
            Sudoku(sizeOfSudoku, missingNumbers)
        }

        countNumbers = Array<Int>(9){0}
        printSudoku()

        Log.i("recreated", "trzy")

        // setters data to view

        val difTextView: TextView = findViewById(R.id.difficultyBtn)
        val misTextView: TextView = findViewById(R.id.mistakes)
        val pointsTextView: TextView = findViewById(R.id.points)
        val timeTextView: TextView = findViewById(R.id.timer)

        difTextView.text = difficulty
        misTextView.text = "$mistakes/3"
        pointsTextView.text = points.toString()


        val hint: LinearLayout = findViewById(R.id.hint)
        val hintImg: ImageView = findViewById(R.id.hintImg)

        if (hints == 1){
            hintImg.setImageResource(R.drawable.ic_light_1)
            // only one hint so off the button
            hint.isClickable = true
        } else {
            hintImg.setImageResource(R.drawable.ic_light_0)
            // only one hint so off the button
            hint.isClickable = false
        }
    }



    override fun onPause() {
        saveTheData()
        super.onPause()
    }


    override fun onStop() {
        saveTheData()
        super.onStop()
    }

    override fun onDestroy() {
        saveTheData()
        super.onDestroy()
    }

    override fun onBackPressed() {
        saveTheData()
        super.onBackPressed()
    }

    // Saving data from board
    private fun saveTheData(){
        stopTimer()

        val sp: SharedPreferences = getSharedPreferences("AppSettingPref", 0)
        val spe: SharedPreferences.Editor = sp.edit()
//        spe.clear()
        // store the data
        Log.d("SAVEtheDATA", isGameOver.toString())
        spe.putBoolean("isgameover", isGameOver)
        spe.putString("difficulty", difficulty)
        spe.putInt("mistakes", mistakes)
        spe.putInt("points", points)
        spe.putInt("iteratorPoints", iteratorPoints)
        spe.putInt("hints", hints)
        spe.putLong("time", time.toLong())
        spe.putBoolean("timeStarted", timerStarted)
        val gson = Gson()
        val json: String = gson.toJson(sudoku)
        spe.putString("sudoku", json)
        spe.apply()
        //TODO OnRestore time not working

    }
}


