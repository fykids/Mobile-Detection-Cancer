package com.dicoding.asclepius.view.fragment.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.databinding.ItemScrollBinding
import com.dicoding.asclepius.helper.NewsDiffCallback
import com.dicoding.asclepius.local.retrofit.response.ArticlesItem

class NewsAdapter : ListAdapter<ArticlesItem, NewsAdapter.NewsViewHolder>(NewsDiffCallback()) {

    inner class NewsViewHolder(private val binding : ItemScrollBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article : ArticlesItem) {
            binding.textViewTitle.text = article.title
            binding.textViewDescription.text = article.description
            Glide.with(binding.imageViewOther.context)
                .load(article.urlToImage)
                .into(binding.imageViewOther)

            binding.root.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : NewsViewHolder {
        val binding = ItemScrollBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder : NewsViewHolder, position : Int) {
        holder.bind(getItem(position))
    }
}
