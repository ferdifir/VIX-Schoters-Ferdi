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
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val searchPreferences: SearchPreferences,
    private val appExecutors: AppExecutors
): INewsRepository {

    override fun getTopHeadlineNews(country: String, category: String): Flow<Resource<List<News>>> =
        object : NetworkBoundResource<List<News>, List<ArticlesItem>>() {
            override suspend fun loadFromDB(): Flow<List<News>> {
                return localDataSource.getListNews(country, category).map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<News>?): Boolean {
                return data == null || data.isEmpty()
            }

            override suspend fun createCall(): Flow<ApiResponse<List<ArticlesItem>>> {
                return remoteDataSource.getTopHeadlineNews(country, category)
            }

            override suspend fun saveCallResult(data: List<ArticlesItem>) {
                val newsList = DataMapper.mapTopNewsResponseToEntities(data, country, category)
                localDataSource.insertNews(newsList)
            }

        }.asFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getSearchNews(): Flow<Resource<List<News>>> =
        object: NetworkBoundResource<List<News>, List<ArticlesItem>>() {
            override suspend fun loadFromDB(): Flow<List<News>> {
                searchPreferences.getNewsQuery().first()
                return localDataSource.getSearchNews(
                    searchPreferences.getNewsQuery().first(),
                    searchPreferences.getNewsCategory().first(),
                    today.toString(),
                    searchPreferences.getNewsDate().first(),
                    searchPreferences.getNewsLanguage().first()
                ).map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<News>?): Boolean {
                return true
            }

            override suspend fun createCall(): Flow<ApiResponse<List<ArticlesItem>>> {
                return remoteDataSource.getSearchNews(
                    searchPreferences.getNewsQuery().first(),
                    searchPreferences.getNewsCategory().first(),
                    today.toString(),
                    searchPreferences.getNewsDate().first(),
                    searchPreferences.getNewsLanguage().first()
                )
            }

            override suspend fun saveCallResult(data: List<ArticlesItem>) {
                val newsList = DataMapper.mapSearchNewsResponseToEntities(
                    data,
                    searchPreferences.getNewsLanguage().first(),
                    searchPreferences.getNewsCategory().first()
                )
                localDataSource.insertNews(newsList)
            }

        }.asFlow()

    override fun setBookmarkedNews(news: News, state: Boolean) {
        val newsEntity = DataMapper.mapDomainToEntity(news)
        appExecutors.diskIO().execute { localDataSource.setBookmarkedNews(newsEntity, state) }
    }

    override fun getBookmarkedNews(): Flow<List<News>> {
        return localDataSource.getBookmarkedNews().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override suspend fun saveSortPreferences(sortener: String) {
        searchPreferences.saveSortPref(sortener)
    }

    override suspend fun saveLanguagePreferences(language: String) {
        searchPreferences.saveLanguagePref(language)
    }

    override suspend fun saveDatePreferences(date: String) {
        searchPreferences.saveDatePref(date)
    }

    override suspend fun saveQueryPreferences(query: String) {
        searchPreferences.saveSearchQuery(query)
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