package com.ferdifir.menitcom.domain.usecase

import com.ferdifir.menitcom.data.source.Resource
import com.ferdifir.menitcom.domain.model.News
import kotlinx.coroutines.flow.Flow

interface NewsUseCase {
    fun getTopHeadlineNews(country: String, category: String): Flow<Resource<List<News>>>
    fun getSearchNews(): Flow<Resource<List<News>>>
    fun setBookmarkedNews(news: News, state: Boolean)
    fun getBookmarkedNews(): Flow<List<News>>

    suspend fun saveSortPreferences(sortener: String)
    suspend fun saveLanguagePreferences(language: String)
    suspend fun saveDatePreferences(date: String)
    suspend fun saveQueryPreferences(query: String)
}