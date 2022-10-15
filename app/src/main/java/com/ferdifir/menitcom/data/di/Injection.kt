package com.ferdifir.menitcom.data.di

import android.content.Context
import com.ferdifir.cory.data.utils.AppExecutors
import com.ferdifir.menitcom.data.source.NewsRepository
import com.ferdifir.menitcom.data.source.local.LocalDataSource
import com.ferdifir.menitcom.data.source.local.room.NewsDatabase
import com.ferdifir.menitcom.data.source.pref.SearchPreferences
import com.ferdifir.menitcom.data.source.pref.dataStore
import com.ferdifir.menitcom.data.source.remote.RemoteDataSource
import com.ferdifir.menitcom.data.source.remote.network.ApiConfig
import com.ferdifir.menitcom.domain.repository.INewsRepository
import com.ferdifir.menitcom.domain.usecase.NewsInteractor
import com.ferdifir.menitcom.domain.usecase.NewsUseCase

object Injection {
    private fun provideRepository(context: Context): INewsRepository {
        val database = NewsDatabase.getInstance(context)

        val remoteDataSource = RemoteDataSource.getInstance(ApiConfig.provideApiService())
        val localDataSource = LocalDataSource.getInstance(database.newsDao())
        val searchPreferences = SearchPreferences.getInstance(context.dataStore)
        val appExecutors = AppExecutors()

        return NewsRepository.getInstance(localDataSource, remoteDataSource, searchPreferences, appExecutors)
    }

    fun provideNewsUseCase(context: Context): NewsUseCase {
        val repository = provideRepository(context)
        return NewsInteractor(repository)
    }
}
