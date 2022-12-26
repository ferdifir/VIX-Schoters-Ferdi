package com.ferdifir.menitcom.data.source

import android.os.Build
import androidx.annotation.RequiresApi
import com.ferdifir.cory.data.utils.AppExecutors
import com.ferdifir.menitcom.data.source.local.LocalDataSource
import com.ferdifir.menitcom.data.source.pref.SearchPreferences
import com.ferdifir.menitcom.data.source.remote.RemoteDataSource
import com.ferdifir.menitcom.data.source.remote.network.ApiResponse
import com.ferdifir.menitcom.data.source.remote.response.ArticlesItem
import com.ferdifir.menitcom.data.utils.DataMapper
import com.ferdifir.menitcom.data.utils.Helper.today
import com.ferdifir.menitcom.domain.model.News
import com.ferdifir.menitcom.domain.repository.INewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class NewsRepository(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource,
    private val pref: SearchPreferences,
    private val executors: AppExecutors
): INewsRepository {

    override fun getTopHeadlineNews(country: String, category: String): Flow<Resource<List<News>>> =
        object : NetworkBoundResource<List<News>, List<ArticlesItem>>() {
            override suspend fun loadFromDB(): Flow<List<News>> {
                return local.getListNews(country, category).map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<News>?): Boolean {
                return true
            }

            override suspend fun createCall(): Flow<ApiResponse<List<ArticlesItem>>> {
                return remote.getTopHeadlineNews(country, category)
            }

            override suspend fun saveCallResult(data: List<ArticlesItem>) {
                val newsList = DataMapper.mapTopNewsResponseToEntities(data, country, category)
                local.insertNews(newsList)
            }

        }.asFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getSearchNews(): Flow<Resource<List<News>>> =
        object: NetworkBoundResource<List<News>, List<ArticlesItem>>() {
            override suspend fun loadFromDB(): Flow<List<News>> {
                pref.getNewsQuery().first()
                return local.getSearchNews(
                    pref.getNewsQuery().first(),
                    pref.getNewsCategory().first(),
                    today.toString(),
                    pref.getNewsDate().first(),
                    pref.getNewsLanguage().first()
                ).map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<News>?): Boolean {
                return true
            }

            override suspend fun createCall(): Flow<ApiResponse<List<ArticlesItem>>> {
                return remote.getSearchNews(
                    pref.getNewsQuery().first(),
                    pref.getNewsCategory().first(),
                    today.toString(),
                    pref.getNewsDate().first(),
                    pref.getNewsLanguage().first()
                )
            }

            override suspend fun saveCallResult(data: List<ArticlesItem>) {
                val newsList = DataMapper.mapSearchNewsResponseToEntities(
                    data,
                    pref.getNewsLanguage().first(),
                    pref.getNewsCategory().first()
                )
                local.insertNews(newsList)
            }

        }.asFlow()

    override fun setBookmarkedNews(news: News, state: Boolean) {
        val newsEntity = DataMapper.mapDomainToEntity(news)
        executors.diskIO().execute { local.setBookmarkedNews(newsEntity, state) }
    }

    override fun getBookmarkedNews(): Flow<List<News>> {
        return local.getBookmarkedNews().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override suspend fun saveSortPreferences(sortener: String) {
        pref.saveSortPref(sortener)
    }

    override suspend fun saveLanguagePreferences(language: String) {
        pref.saveLanguagePref(language)
    }

    override suspend fun saveDatePreferences(date: String) {
        pref.saveDatePref(date)
    }

    override suspend fun saveQueryPreferences(query: String) {
        pref.saveSearchQuery(query)
    }

    companion object {
        @Volatile
        private var instance: NewsRepository? = null

        fun getInstance(
            localData: LocalDataSource,
            remoteData: RemoteDataSource,
            searchPref: SearchPreferences,
            appExecutors: AppExecutors
        ): NewsRepository =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(localData, remoteData, searchPref, appExecutors)
            }
    }
}