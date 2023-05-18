package com.example.enqer.sudoku.ui.main


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.enqer.sudoku.sqlite.SQLiteManager

class PageViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()
//    val text: LiveData<String> = Transformations.map(_index) {
//        "Hello world from section: $it"
//        Log.d("text", it.toString()).toString()
//    }

    fun setIndex(index: Int) {
        _index.value = index
    }
}