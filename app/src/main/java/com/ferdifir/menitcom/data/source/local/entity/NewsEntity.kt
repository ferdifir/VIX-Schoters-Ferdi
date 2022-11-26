package com.ferdifir.menitcom.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(

	@ColumnInfo(name = "publishedAt")
	val publishedAt: String? = null,

	@ColumnInfo(name = "author")
	val author: String? = null,

	@ColumnInfo(name = "urlToImage")
	val urlToImage: String? = null,

	@ColumnInfo(name = "description")
	val description: String? = null,

	@ColumnInfo(name = "name")
	val sourceName: String? = null,

	@ColumnInfo(name = "title")
	val title: String? = null,

	@PrimaryKey
	@ColumnInfo(name = "url")
	val url: String,

	@ColumnInfo(name = "content")
	val content: String? = null,

	@ColumnInfo(name = "isBookmarked")
	var isBookmarked: Boolean = false,

	@ColumnInfo(name = "category")
	var category: String? = null,

	@ColumnInfo(name = "country")
	var country: String? = null,

	@ColumnInfo(name = "searchNews")
	var searchNews: Boolean,

	@ColumnInfo(name = "language")
	var language: String? = null,

	@ColumnInfo(name = "sortener")
	var sortener: String? = null
)