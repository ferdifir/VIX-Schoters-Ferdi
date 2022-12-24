package com.ferdifir.menitcom.data.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ferdifir.menitcom.data.di.Injection
import com.ferdifir.menitcom.domain.usecase.NewsUseCase
import com.ferdifir.menitcom.presentation.main.search.BookmarkViewModel
import com.ferdifir.menitcom.presentation.detail.DetailViewModel
import com.ferdifir.menitcom.presentation.explore.ExploreViewModel
import com.ferdifir.menitcom.presentation.main.news.HomeViewModel

class ViewModelFactory private constructor(private val newsUseCase: NewsUseCase) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideNewsUseCase(context)
                )
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(newsUseCase) as T
            }
            modelClass.isAssignableFrom(BookmarkViewModel::class.java) -> {
                BookmarkViewModel(newsUseCase) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(newsUseCase) as T
            }
            modelClass.isAssignableFrom(ExploreViewModel::class.java) -> {
                ExploreViewModel(newsUseCase) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}