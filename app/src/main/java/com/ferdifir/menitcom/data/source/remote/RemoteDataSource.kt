package com.ferdifir.menitcom.data.source.remote

import android.util.Log
import com.ferdifir.menitcom.BuildConfig.ApiKey
import com.ferdifir.menitcom.data.source.remote.network.ApiResponse
import com.ferdifir.menitcom.data.source.remote.network.ApiService
import com.ferdifir.menitcom.data.source.remote.response.ArticlesItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@Suppress("UNCHECKED_CAST")
class RemoteDataSource private constructor(private val apiService: ApiService){

    suspend fun getTopHeadlineNews(country: String, category: String): Flow<ApiResponse<List<ArticlesItem>>> {
        return flow {
            try {
                val response = apiService.getTopHeadlineNews(ApiKey, country, category)
                val dataArray = response.articles
                if (dataArray != null) {
                    if (dataArray.isNotEmpty()) {
                        emit(ApiResponse.Success(response.articles))
                    } else {
                        emit(ApiResponse.Empty)
                    }
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("Top Headline", e.toString())
            }
        }.flowOn(Dispatchers.IO) as Flow<ApiResponse<List<ArticlesItem>>>
    }

    suspend fun getSearchNews(
        query: String,
        startDate: String,
        finalDate: String,
        sortedBy: String,
        language: String
    ): Flow<ApiResponse<List<ArticlesItem>>> {
        return flow {
            try {
                val response = apiService.getAllNews(
                    ApiKey, query, startDate, finalDate, sortedBy, language
                )
                val dataArray = response.articles
                if (dataArray != null) {
                    if (dataArray.isNotEmpty()) {
                        emit(ApiResponse.Success(response.articles))
                    } else {
                        emit(ApiResponse.Empty)
                    }
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("Top Headline", e.toString())
            }
        }.flowOn(Dispatchers.IO) as Flow<ApiResponse<List<ArticlesItem>>>
    }

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(service: ApiService): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(service)
            }
    }

}