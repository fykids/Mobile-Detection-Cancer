package com.dicoding.asclepius.view.fragment.history

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.local.database.History
import com.dicoding.asclepius.local.repository.HistoryRepository

class HistoryViewModel(application : Application) : ViewModel() {
    private val mHistoryRepository : HistoryRepository = HistoryRepository(application)

    fun getAllHistory() : LiveData<List<History>> = mHistoryRepository.getAllHistory()
}