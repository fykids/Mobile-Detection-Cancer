package com.dicoding.asclepius.view.fragment.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.FragmentNewsBinding
import com.dicoding.asclepius.view.fragment.adapter.NewsAdapter

class NewsFragment : Fragment() {

    private val viewModel : NewsViewModel by viewModels()
    private lateinit var binding : FragmentNewsBinding
    private val newsAdapter = NewsAdapter()

    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?,
    ) : View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)

        // Setup RecyclerView dengan Adapter
        binding.recyclerViewNews.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewNews.adapter = newsAdapter

        // Observasi LiveData dari ViewModel
        viewModel.healthResponse.observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                // Update adapter dengan artikel baru
                newsAdapter.submitList(it.articles)
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                // Tampilkan pesan error (misalnya dengan Toast)
                Toast.makeText(requireContext(), "Error Bray", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { loading ->
            // Menampilkan atau menyembunyikan progress bar berdasarkan status loading
            if (loading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        // Memanggil API untuk mendapatkan data
        val apiKey = "706e000a115d46bc8d845e85d0bb58db" // Ganti dengan API key Anda
        val query = "cancer" // Misalnya mencari artikel tentang kesehatan
        val category = "health" // Kategori artikel
        val language = "en" // Bahasa artikel
        viewModel.fetchArticles(apiKey, query, category, language)

        return binding.root
    }
}
