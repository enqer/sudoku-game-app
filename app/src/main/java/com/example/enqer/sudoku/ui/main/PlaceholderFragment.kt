package com.example.enqer.sudoku.ui.main

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.enqer.sudoku.R
import com.example.enqer.sudoku.databinding.FragmentStatsBinding
import com.example.enqer.sudoku.interfaces.TimeFormatter
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
            }else if(arguments?.getInt(ARG_SECTION_NUMBER) == 2){
                games = sqLiteManager.getGamesPlayedByDifficulty(MEDIUM)
                wins = sqLiteManager.getWinGamesByDifficulty(MEDIUM)
                mistakes = sqLiteManager.getAvgMistakes(MEDIUM)
                mostPoints = sqLiteManager.getMostPoints(MEDIUM)
                allPoints = sqLiteManager.getAllPoints(MEDIUM)
                bestTime = sqLiteManager.getBestTime(MEDIUM)
                avgTime = sqLiteManager.getAvgTime(MEDIUM)
            }else {
                games = sqLiteManager.getGamesPlayedByDifficulty(HARD)
                wins = sqLiteManager.getWinGamesByDifficulty(HARD)
                mistakes = sqLiteManager.getAvgMistakes(HARD)
                mostPoints = sqLiteManager.getMostPoints(HARD)
                allPoints = sqLiteManager.getAllPoints(HARD)
                bestTime = sqLiteManager.getBestTime(HARD)
                avgTime = sqLiteManager.getAvgTime(HARD)
            }
            binding.startedGames.text = if (games == 0) "0" else games.toString()
            binding.winnerGames.text = if (games == 0) "0" else wins.toString()
            binding.percentGames.text = if (wins == 0) "0" else String.format("%.1f",(wins.toDouble()/games.toDouble())*100)+"%"
            binding.avgMistakesGames.text = if (mistakes == 0) "0" else String.format("%.1f",(mistakes.toDouble()/games.toDouble()))
            binding.maxPointsGames.text = if (games == 0) "0" else mostPoints.toString()
            binding.avgPointsGames.text =  if (allPoints == 0) "0" else String.format("%.1f",(allPoints.toDouble()/wins.toDouble()))
            binding.bestTimeGames.text = if (bestTime.toInt() == 0) "0" else TimeFormatter.getTimeStringFromDouble(bestTime.toDouble())
            binding.avgTimeGames.text = if (avgTime.toInt() == 0) "0" else TimeFormatter.getTimeStringFromDouble((avgTime.toDouble()/wins.toDouble()))
        }catch (e: Exception){
            e.printStackTrace()

        }

//        val textView: TextView = binding.sectionLabel
//        pageViewModel.text.observe(viewLifecycleOwner, Observer {
//            it
//        })

        binding.deleteStats.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
            dialog.setMessage("Czy na pewno chcesz zresetować statystyki?")
            dialog.setPositiveButton("Tak") { p0, p1 ->
                if (arguments?.getInt(ARG_SECTION_NUMBER) == 1)
                    sqLiteManager.deleteStats(EASY)
                else if (arguments?.getInt(ARG_SECTION_NUMBER) == 2)
                    sqLiteManager.deleteStats(MEDIUM)
                else if (arguments?.getInt(ARG_SECTION_NUMBER) == 3)
                    sqLiteManager.deleteStats(HARD)
                binding.startedGames.text = "0"
                binding.winnerGames.text = "0"
                binding.percentGames.text = "0"
                binding.avgMistakesGames.text = "0"
                binding.maxPointsGames.text = "0"
                binding.avgPointsGames.text = "0"
                binding.bestTimeGames.text = "0"
                binding.avgTimeGames.text = "0"
            }
            dialog.show()

        }

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

}