package com.ferdifir.menitcom.domain.usecase

import com.ferdifir.menitcom.data.source.Resource
import com.ferdifir.menitcom.domain.model.News
import com.ferdifir.menitcom.domain.repository.INewsRepository
import kotlinx.coroutines.flow.Flow

class NewsInteractor(private val repository: INewsRepository): NewsUseCase {
    override fun getTopHeadlineNews(country: String, category: String): Flow<Resource<List<News>>> =
        repository.getTopHeadlineNews(country, category)

    override fun getSearchNews(): Flow<Resource<List<News>>> =
        repository.getSearchNews()

    override fun setBookmarkedNews(news: News, state: Boolean) =
        repository.setBookmarkedNews(news, state)

    override fun getBookmarkedNews(): Flow<List<News>> =
        repository.getBookmarkedNews()

    override suspend fun saveSortPreferences(sortener: String) =
        repository.saveSortPreferences(sortener)

    override suspend fun saveLanguagePreferences(language: String) =
        repository.saveLanguagePreferences(language)

    override suspend fun saveDatePreferences(date: String) =
        repository.saveDatePreferences(date)

    override suspend fun saveQueryPreferences(query: String) =
        repository.saveQueryPreferences(query)
}