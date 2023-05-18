package com.example.enqer.sudoku.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.enqer.sudoku.MainActivity
import com.example.enqer.sudoku.R
import com.example.enqer.sudoku.databinding.FragmentStatsBinding
import com.example.enqer.sudoku.sqlite.SQLiteManager
import kotlin.math.roundToInt

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private val EASY = "easy"
    private val MEDIUM = "medium"
    private val HARD = "hard"
    private lateinit var sqLiteManager: SQLiteManager
    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentStatsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this)[PageViewModel::class.java].apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        val root = binding.root
        sqLiteManager = SQLiteManager(this.requireContext())
        binding.startedGames.text = sqLiteManager.getGamesPlayedByDifficulty("Trudna").toString()


//        Log.d("test",arguments?.getInt(ARG_SECTION_NUMBER).toString())
        var games: Int
        var wins: Int
        var mistakes: Int
        var mostPoints: Int
        var allPoints: Int
        var bestTime: Long
        var avgTime: Long
        try {
            if (arguments?.getInt(ARG_SECTION_NUMBER) == 1){
                games = sqLiteManager.getGamesPlayedByDifficulty(EASY)
                wins = sqLiteManager.getWinGamesByDifficulty(EASY)
                mistakes = sqLiteManager.getAvgMistakes(EASY)
                mostPoints = sqLiteManager.getMostPoints(EASY)
                allPoints = sqLiteManager.getAllPoints(EASY)
                bestTime = sqLiteManager.getBestTime(EASY)
                avgTime = sqLiteManager.getAvgTime(EASY)
                Log.d("gry", sqLiteManager.getAvgTime(EASY).toString())
                binding.startedGames.text = games.toString()
                binding.winnerGames.text = wins.toString()
                binding.percentGames.text = (wins/games).toString()
                binding.avgMistakesGames.text = (mistakes/games).toString()
                binding.maxPointsGames.text = mostPoints.toString()
                binding.avgPointsGames.text =  String.format("%.1f",(allPoints.toDouble()/games.toDouble()))
                binding.bestTimeGames.text = getTimeStringFromDouble(bestTime.toDouble())
                binding.avgTimeGames.text = getTimeStringFromDouble((avgTime.toDouble()/wins.toDouble()))
            }else{
                binding.winnerGames.text = "4"
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

//        val textView: TextView = binding.sectionLabel
//        pageViewModel.text.observe(viewLifecycleOwner, Observer {
//            it
//        })



        return root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60
        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Int, minutes: Int, seconds: Int): String = String.format("%02d:%02d:%02d", hours, minutes, seconds)


}