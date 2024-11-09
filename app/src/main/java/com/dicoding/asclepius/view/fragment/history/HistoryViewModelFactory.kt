package com.dicoding.asclepius.view.fragment.history

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.view.fragment.home.HomeViewModel

class HistoryViewModelFactory private constructor(private val mApplication : Application) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE : HistoryViewModelFactory? = null

        @JvmStatic
        fun getInstance(application : Application) : HistoryViewModelFactory {
            if (INSTANCE == null) {
                synchronized(HistoryViewModelFactory::class.java) {
                    INSTANCE = HistoryViewModelFactory(application)
                }
            }

            return INSTANCE as HistoryViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass : Class<T>) : T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(mApplication) as T
        }

        throw IllegalArgumentException("Unkwon ViewModel class: ${modelClass.name}")
    }
}