package com.dicoding.asclepius.view.fragment.home

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.local.database.History
import com.dicoding.asclepius.local.repository.HistoryRepository

class HomeViewModel(application : Application) : ViewModel() {
    private val mHistoryRepository : HistoryRepository = HistoryRepository(application)

    fun insert(history : History) {
        mHistoryRepository.insert(history)
    }
}