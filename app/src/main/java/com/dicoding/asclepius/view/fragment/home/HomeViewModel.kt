package com.dicoding.asclepius.view.fragment.home

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.local.database.History
import com.dicoding.asclepius.local.repository.HistoryRepository

class HomeViewModel(application : Application) : ViewModel() {
    private val mHistoryRepository : HistoryRepository = HistoryRepository(application)

    private val _currentImageUri = MutableLiveData<Uri?>()
    val currentImageUri : LiveData<Uri?> get() = _currentImageUri

    fun setCurrentImageUri(uri : Uri?) {
        _currentImageUri.value = uri
    }

    fun insert(history : History) {
        mHistoryRepository.insert(history)
    }
}