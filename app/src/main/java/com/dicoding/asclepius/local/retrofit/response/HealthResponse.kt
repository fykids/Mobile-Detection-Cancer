package com.dicoding.asclepius.local.retrofit.response

import com.google.gson.annotations.SerializedName

data class HealthResponse(

	@field:SerializedName("totalResults")
	val totalResults: Int,

	@field:SerializedName("articles")
	val articles: List<ArticlesItem>? = null, // Menambahkan ? agar nullable

	@field:SerializedName("status")
	val status: String? = null // Menambahkan ? agar nullable
)

data class ArticlesItem(

	@field:SerializedName("publishedAt")
	val publishedAt: String? = null, // Menambahkan ? agar nullable

	@field:SerializedName("author")
	val author: Any? = null, // Menambahkan ? agar nullable

	@field:SerializedName("urlToImage")
	val urlToImage: String? = null, // Menambahkan ? agar nullable

	@field:SerializedName("description")
	val description: String? = null, // Menambahkan ? agar nullable

	@field:SerializedName("source")
	val source: Source? = null, // Menambahkan ? agar nullable

	@field:SerializedName("title")
	val title: String? = null, // Menambahkan ? agar nullable

	@field:SerializedName("url")
	val url: String? = null, // Menambahkan ? agar nullable

	@field:SerializedName("content")
	val content: String? = null // Menambahkan ? agar nullable
)

data class Source(

	@field:SerializedName("name")
	val name: String? = null, // Menambahkan ? agar nullable

	@field:SerializedName("id")
	val id: Any? = null // Menambahkan ? agar nullable
)
