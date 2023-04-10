package com.example.enqer.sudoku

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import org.w3c.dom.Text

class GameActivity : AppCompatActivity() {
    companion object{

        @SuppressLint("StaticFieldLeak")
        lateinit var pointedBtn: Button // the button which is selected
        // coords of pointedBtn
        var x: Int = 0;
        var y: Int = 0;
        val isPointedBtnInit get() = this::pointedBtn.isInitialized
        const val coords = "abcdefghi" // coords of board (from top to bottom)

        // Sudoku object and every data from board
        lateinit var sudoku: Sudoku
        const val sizeOfSudoku: Int = 9
        lateinit var difficulty: String
        var missingNumbers: Int = 40
        var mistakes: Int = 0
        var points: Int = 0
        var iteratorPoints: Int = 1
        lateinit var  countNumbers : Array<Int> // count which number is full on board
        var isNotesMode = false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

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

        // setting difficulty of the game
        difficulty = intent.getStringExtra("diff").toString()
        if (difficulty.equals("Łatwy")){
            missingNumbers = 40
            difficulty = "Łatwa"
        } else if (difficulty.equals("Średni")){
            missingNumbers = 50
            difficulty = "Średnia"
        } else {
            missingNumbers = 60
            difficulty = "Trudna"
        }
        val difficultyBtn: TextView = findViewById(R.id.difficultyBtn)
        difficultyBtn.text = difficulty

        // creating object of the game and small setup
        sudoku = Sudoku(sizeOfSudoku, missingNumbers)
        countNumbers = Array<Int>(9){0}
        printSudoku()


        // TODO zrobić liczenie czasu i wyświetlić go
        // TODO przycisk z wróceniem do poprzedniego activity finishActivity() na onclicku ale trzeba zapisać stan
        // TODO do powyższego zapisać stan w sharedpref i wtedy sprawdzać i chyba pobierać z db dane z ostatniej gry
        // TODO do powyższego albo w ssharedpref trzymać dane z gry + zmienna isGame i sprawdzać jak jest to jest btn i można wrócić
        // TODO baza danych tylko dla statystyk??? chyba ta
        // TODO Zakończenie gry
        // TODO wyświetlic informacje że nie można już zmienić znaku który jest gites przy wszystkich funkcjach lul


        // clearing the field
        val clear: LinearLayout = findViewById(R.id.clear)
        clear.setOnClickListener {
            if (isPointedBtnInit && sudoku.mat[x][y] != sudoku.fullMat[x][y]) {
                pointedBtn.text = ""
            }
        }

        //TODO przetestować czy jak klikniemy w notatki przy ostatnim numerze to zniknie cyz nie
        // setting notes
        val notes: LinearLayout = findViewById(R.id.notes)
        notes.setOnClickListener{
            isNotesMode = !isNotesMode  // true - is in notes mode
            for (i in 1..9){
                val id = resources.getIdentifier("num$i", "id", packageName)
                val btn: Button = findViewById(id)
                if (isNotesMode){
                    btn.setTextColor(ContextCompat.getColor(applicationContext, R.color.grey))
                } else{
                    btn.setTextColor(ContextCompat.getColor(applicationContext, R.color.granat))

                }
            }

        }

        // getting hint
        // TODO img z 1 po kliknieciu i pokazaniu hint zmienić src na img z 0 że 0 podpowiedzi już jest
        val hint: LinearLayout = findViewById(R.id.hint)
        hint.setOnClickListener {
            if (isPointedBtnInit && sudoku.mat[x][y] != sudoku.fullMat[x][y]) {
                pointedBtn.text = sudoku.fullMat[x][y].toString()
                // changing border
                pointedBtn.setBackgroundResource(R.drawable.border_marked_hint)
                pointedBtn.setTextColor(ContextCompat.getColor(applicationContext, R.color.littleBlack))
                pointedBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32F)
                sudoku.mat[x][y] = sudoku.fullMat[x][y]

                //changing img of hint
                val hintImg: ImageView = findViewById(R.id.hintImg)
                hintImg.setImageResource(R.drawable.ic_light_0)
                // only one hint so off the button
                hint.isClickable = false

            }
        }

    }

    // selecting the fileds to set a colors
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
                        btn.setBackgroundResource(R.drawable.border_marked_same)
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
//        var tag: String
        if (isPointedBtnInit){  // if button is initialized then we can do some things
            if (isNotesMode && sudoku.mat[x][y].toString() != pointedBtn.text.toString()){
                // Only when the hasnt good value
                //btn.isClicable
//                if (sudoku.mat[x][y] != pointedBtn.text.toString().toInt()){
//                    tag = btnNumber.text.toString()
                    val notes = Array<String>(9){" "}

                    // TODO jeśli wcześniej jest dobra wartość to wyłączy pole fix it
                    var index = 0
//                    val pointedText = pointedBtn.text.toString()
                    for (i in "123456789"){
                        if (i.toString() in pointedBtn.text.toString()){
                            notes[index++] = i.toString()
                        } else {
                            notes[index++] = " "
                        }
//                        notes.forEach { Log.i("NOTES/" ,it) }
//                        Log.i("NOTES/" ,"=============")
                    }
                    // adding new value to notes of field
                    val addToNotes = btnNumber.text.toString()
                    if (addToNotes == notes[addToNotes.toInt()-1]){
                        notes[addToNotes.toInt()-1] = " "
                    } else {
                        notes[addToNotes.toInt()-1] = addToNotes
                    }
//                }
                    // notes has a different styles
                    pointedBtn.setTextColor(ContextCompat.getColor(applicationContext, R.color.grey))
                    pointedBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11F)

                    // 2 space between letter(number) and 2 space instead of letter(number)
                    var notesBtn = ""
                    // print the note in the field
                    var counter = 0
                    for (i in notes){
//                    if (i == " "){
//                        notesBtn += '\u00A0'
//                    } else{
//                        notesBtn += i
//                    }
                        notesBtn += i
                        if (counter == 2 || counter == 5){
                            notesBtn += "\n"
                        }
                        counter++
                    }
                    pointedBtn.text = notesBtn
//                }

            } else {
//                if (sudoku.mat[x][y] != pointedBtn.text.toString().toInt()) {
////                    tag = pointedBtn.tag.toString()
//                    pointedBtn.text = btnNumber.text
//                    val i = coords.indexOf(tag[0])
//                    val j = tag[1].toString().toInt() - 1
//                    Log.i("Sudo", sudoku.fullMat[x][y].toString())
//                    pointedBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32F)
                    if (sudoku.fullMat[x][y] == btnNumber.text.toString().toInt()) {
//                        pointedBtn.isClickable = false
                        pointedBtn.text = btnNumber.text
                        points += (1..9).random() * iteratorPoints
                        iteratorPoints += 2
                        val pointsView: TextView = findViewById(R.id.points)
                        pointsView.text = points.toString()
                        pointedBtn.setTextColor(ContextCompat.getColor(applicationContext, R.color.littleBlack))

                        sudoku.mat[x][y] = pointedBtn.text.toString().toInt()

                        // Checking if number is printed 9 times and then disappear
                        countNumbers[sudoku.mat[x][y] - 1]++
                        Log.i("ARRAY", countNumbers[sudoku.mat[x][y] - 1].toString())
                        if (countNumbers[sudoku.mat[x][y] - 1] == 9) {
                            btnNumber.text = ""
                            btnNumber.isClickable = false
                        }
                        if (sudoku.fullMat contentEquals sudoku.mat) {
                            Log.i("WINNER", "Smiga")
                            // TODO winner
                            // wtedy ma się coś zdażyć wygrana może popup a może komunikat i wyjście po czasie idk
                            // zapis do bazy danych żeby pamiętać wyniki i dla statystyk
                        }
                    } else {
                        if (sudoku.mat[x][y] != sudoku.fullMat[x][y]) {
                            pointedBtn.text = btnNumber.text
                            mistakes++
                            val miss: TextView = findViewById(R.id.mistakes)
                            miss.text = "$mistakes/3"
                            pointedBtn.setTextColor(ContextCompat.getColor(applicationContext, R.color.mistake))

                            if (mistakes == 3) {
                                //TODO przegrana koniec gry do zrobienia
                            }
                        }
                    }
//                }
                pointedBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32F)
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


    // Saving instance of data
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var btn: Button
        var id: Int
        val data = ArrayList<String>()
        Log.i("SAVE","WORKS")
        for (i in coords) {
            for (j in 1..9) {
                id = resources.getIdentifier("$i$j", "id", packageName)
                btn = findViewById(id)
                data.add(btn.text.toString())
            }
        }
        outState.putStringArrayList("data", data)
        outState.putInt("miastakes", mistakes)
        outState.putInt("points", points)
        // TODO save and restore czas i inne pierdoły i cały board też przy finishActivity

    }

    // Restoring instance of data
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("RESTORE","WORKS")
        val miss = savedInstanceState.getInt("mistakes",0)
        val po = savedInstanceState.getInt("points",0)
        val data = savedInstanceState.getStringArrayList("data") as ArrayList<String>
        var btn: Button
        var id: Int
        Log.i("SAVE","WORKS")
        var index = 0
        for (i in coords) {
            for (j in 1..9) {
                id = resources.getIdentifier("$i$j", "id", packageName)
                btn = findViewById(id)
                btn.text = data[index].toString()
                index++
            }
        }
        val m: TextView = findViewById(R.id.mistakes)
        m.text = miss.toString()
        val p: TextView = findViewById(R.id.points)
        p.text = po.toString()
    }




}


