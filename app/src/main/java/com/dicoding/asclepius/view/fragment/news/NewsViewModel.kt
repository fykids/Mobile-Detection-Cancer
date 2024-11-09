package com.dicoding.asclepius.view.fragment.news

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.asclepius.local.retrofit.api.ApiCofig
import com.dicoding.asclepius.local.retrofit.response.HealthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel(application : Application) : AndroidViewModel(application) {

    private val _healthResponse = MutableLiveData<HealthResponse>()
    val healthResponse : LiveData<HealthResponse> get() = _healthResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> get() = _isLoading

    private val apiService =
        ApiCofig.getApiService()  // Menggunakan ApiCofig untuk mendapatkan ApiService

    fun fetchArticles(apiKey : String, query : String, category : String, language : String) {
        _isLoading.value = true

        // Memanggil API untuk mendapatkan artikel
        apiService.getTopHeadlines(query, category, language, apiKey)
            .enqueue(object : Callback<HealthResponse> {
                override fun onResponse(
                    call : Call<HealthResponse>,
                    response : Response<HealthResponse>,
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        // Menyimpan data yang diterima ke LiveData
                        _healthResponse.value = response.body()
                    } else {
                        // Menangani error jika response gagal
                        _errorMessage.value = "Error: ${response.message()}"
                    }
                }

                override fun onFailure(call : Call<HealthResponse>, t : Throwable) {
                    _isLoading.value = false
                    _errorMessage.value = "Failed to load data: ${t.message}"
                }
            })
    }
}
