package com.dicoding.asclepius.view.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.databinding.ItemScrollBinding
import com.dicoding.asclepius.helper.HistoryDiffCallback
import com.dicoding.asclepius.local.database.History

class HistoryAdapter :
    ListAdapter<History, HistoryAdapter.HistoryViewHolder>(HistoryDiffCallback()) {

    inner class HistoryViewHolder(private val binding : ItemScrollBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(history : History) {
            binding.textViewTitle.text = history.results // Menampilkan hasil dari database
            binding.textViewDescription.text = history.date // Menampilkan tanggal sebagai deskripsi
            Glide.with(binding.imageViewOther.context)
                .load(history.imageClassifier) // Menggunakan imageClassifier untuk gambar
                .into(binding.imageViewOther)
        }
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : HistoryViewHolder {
        val binding = ItemScrollBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder : HistoryViewHolder,
        position : Int,
    ) {
        holder.bind(getItem(position))
    }
}
