package com.example.enqer.sudoku



import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
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
        val isPointedBtnInit get() = this::pointedBtn.isInitialized
        val coords = "abcdefghi" // coords of board (from top to bottom)


        // Sudoku object and every data from board
        lateinit var sudoku: Sudoku
        const val sizeOfSudoku: Int = 9
        lateinit var difficulty: String
        var missingNumbers: Int = 40
        var mistakes: Int = 0
        var points: Int = 0
        var iteratorPoints: Int = 1
        val countNumbers = Array<Int>(9){0} // count which number is full on board

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

        sudoku = Sudoku(sizeOfSudoku, missingNumbers)
        printSudoku()

        for (i in 0 until sizeOfSudoku) {
            for (j in 0 until sizeOfSudoku) {
                var n = sudoku.mat[i][j]
                if (n.toString() != "0"){
                    countNumbers[n-1]++
                    if (countNumbers[n-1] == 9){
                        val id = resources.getIdentifier("num$n", "id", packageName)
                        val btn: Button = findViewById(id)
                        btn.text = ""
                        btn.isClickable = false
                    }
                }
            }
        }

        for(i in countNumbers){
            Log.i("ARRAY/INDEX",  i.toString())
        }


        // TODO zrobić liczenie czasu i wyświetlić go
        // TODO Notatki będzie problem
        // TODO kliknięcie na np 5 podświetla wszystkie 5 imo git dla grania
        // TODO jeśli wszystkie np 5 są na boardzie to usunąć z dołu tą 5
        // TODO przycisk z wróceniem do poprzedniego activity finishActivity() na onclicku ale trzeba zapisać stan
        // TODO do powyższego zapisać stan w sharedpref i wtedy sprawdzać i chyba pobierać z db dane z ostatniej gry
        // TODO do powyższego albo w ssharedpref trzymać dane z gry + zmienna isGame i sprawdzać jak jest to jest btn i można wrócić
        // TODO baza danych tylko dla statystyk??? chyba ta
        // TODO Zakończenie gry


        // clearing the field
        val clear: LinearLayout = findViewById(R.id.clear)
        clear.setOnClickListener {
            if (isPointedBtnInit) {
                if (!pointedBtn.isClickable){
                    val s: String = pointedBtn.text.toString()
                    countNumbers[s.toInt()-1]--
                    val id = resources.getIdentifier("num$s", "id", packageName)
                    val btn: Button = findViewById(id)
                    btn.text = s
                    btn.isClickable = true

                }
                pointedBtn.text = ""
                pointedBtn.isClickable = true
            }
        }

        // getting hint
        // TODO img z 1 po kliknieciu i pokazaniu hint zmienić src na img z 0 że 0 podpowiedzi już jest
        val hint: LinearLayout = findViewById(R.id.hint)
        hint.setOnClickListener {
            if (isPointedBtnInit) {
                val tag: String = pointedBtn.tag.toString()
                val i = coords.indexOf(tag[0])
                val j = tag[1].toString().toInt() - 1
                pointedBtn.text = sudoku.fullMat[i][j].toString()
                pointedBtn.isClickable = false
                // changing border
                pointedBtn.setBackgroundResource(R.drawable.border_marked_hint)
                pointedBtn.setTextColor(ContextCompat.getColor(applicationContext, R.color.littleBlack))
                sudoku.mat[i][j] = sudoku.fullMat[i][j]

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
            var id: Int
            var btn: Button

            // cleaning the board
            for (i in coords) {
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
        val tag: String
        if (isPointedBtnInit){
            tag = pointedBtn.tag.toString()
            pointedBtn.text = btnNumber.text
            val i = coords.indexOf(tag[0])
            val j = tag[1].toString().toInt()-1
            Log.i("I", i.toString())
            Log.i("J", j.toString())
            Log.i("Sudo", sudoku.fullMat[i][j].toString())
            if (sudoku.fullMat[i][j] == pointedBtn.text.toString().toInt()) {
                pointedBtn.isClickable = false
                points+= (1..9).random() * iteratorPoints
                iteratorPoints+=2
                val pointsView: TextView = findViewById(R.id.points)
                pointsView.text = points.toString()
                pointedBtn.setTextColor(ContextCompat.getColor(applicationContext, R.color.littleBlack))
                sudoku.mat[i][j] = pointedBtn.text.toString().toInt()

                // Checking if number is printed 9 times and then disappear
                countNumbers[sudoku.mat[i][j]-1]++
                Log.i("ARRAY",  countNumbers[sudoku.mat[i][j]-1].toString())
                if (countNumbers[sudoku.mat[i][j]-1] == 9){
                        btnNumber.text = ""
                        btnNumber.isClickable = false
                }



                if (sudoku.fullMat contentEquals sudoku.mat){
                    Log.i("WINNER", "Smiga")
                    // TODO winner
                    // wtedy ma się coś zdażyć wygrana może popup a może komunikat i wyjście po czasie idk
                    // zapis do bazy danych żeby pamiętać wyniki i dla statystyk
                }
            } else {
                // secure by changing number with good num in good field
                if (!pointedBtn.isClickable){
                    val s: String = sudoku.fullMat[i][j].toString()
                    countNumbers[s.toInt()-1]--
                    val id = resources.getIdentifier("num$s", "id", packageName)
                    val btn: Button = findViewById(id)
                    btn.text = s
                    btn.isClickable = true

                }
                pointedBtn.isClickable = true
                mistakes++
                val miss: TextView = findViewById(R.id.mistakes)
                miss.text = "$mistakes/3"
//                pointedBtn.setBackgroundResource(R.drawable.border_marked_mistake)
                pointedBtn.setTextColor(ContextCompat.getColor(applicationContext, R.color.mistake))
                if (mistakes == 3){
                    //TODO przegrana koniec gry do zrobienia
                }

            }
        }
//        pointedBtn.text = btnNumber.text
    }

    fun printSudoku(){
        var id: Int
        var btn: Button

        var k = 0
        // cleaning the board
        for (i in coords) {
            for (j in 1..9) {
                id = resources.getIdentifier("$i$j", "id", packageName)
                btn = findViewById(id)
                if (sudoku.mat[k][j-1].toString() == "0"){
                    btn.text = ""
                }else{
                    btn.text= sudoku.mat[k][j-1].toString()
                    btn.isClickable = false
                    // zmienić to żeby klikać i wtedy podświetlają się wszystkie liczby danego wyboru
                }

            }
            k++
        }
    }

    // Saving and restoring data
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
        // TODO save and restore czas

    }

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


