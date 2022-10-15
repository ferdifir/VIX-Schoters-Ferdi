package com.ferdifir.menitcom.data.source.local

import com.ferdifir.menitcom.data.source.local.entity.NewsEntity
import com.ferdifir.menitcom.data.source.local.room.NewsDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource private constructor(private val newsDao: NewsDao){

    fun getListNews(country: String, category: String): Flow<List<NewsEntity>> = newsDao.getTopNews(category, country)

    fun getSearchNews(query: String, sortedBy: String, startDate: String, finalDate: String, language: String):
            Flow<List<NewsEntity>> = newsDao.getSearchNews(query, sortedBy, startDate, finalDate, language)

    fun getBookmarkedNews(): Flow<List<NewsEntity>> = newsDao.getBookmarkedNews()

    suspend fun insertNews(news: List<NewsEntity>) = newsDao.insertNews(news)

    fun setBookmarkedNews(news: NewsEntity, newState: Boolean) {
        news.isBookmarked = newState
        newsDao.updateNews(news)
    }

    companion object {
        private var instance: LocalDataSource? = null

        fun getInstance(newsDao: NewsDao): LocalDataSource =
            instance ?: synchronized(this) {
                instance ?: LocalDataSource(newsDao)
            }
    }
}