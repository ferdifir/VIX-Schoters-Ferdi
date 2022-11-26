package com.ferdifir.menitcom.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
	val publishedAt: String? = null,
	val author: String? = null,
	val urlToImage: String? = null,
	val description: String? = null,
	val sourceName: String? = null,
	val title: String? = null,
	val url: String,
	val content: String? = null,
	var isBookmarked: Boolean,
	var category: String? = null,
	var country: String? = null,
	var searchNews: Boolean,
	var language: String? = null,
	var sortener: String? = null
): Parcelable