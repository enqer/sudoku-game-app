package com.example.enqer.sudoku.interfaces

import kotlin.math.roundToInt

interface TimeFormatter {

    companion object{
        fun getTimeStringFromDouble(time: Double): String {
            val resultInt = time.roundToInt()
            val hours = resultInt % 86400 / 3600
            val minutes = resultInt % 86400 % 3600 / 60
            val seconds = resultInt % 86400 % 3600 % 60
            return makeTimeString(hours, minutes, seconds)
        }

        fun makeTimeString(hours: Int, minutes: Int, seconds: Int): String = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    }

}