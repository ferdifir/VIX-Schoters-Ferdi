package com.ferdifir.menitcom.presentation.main.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ferdifir.menitcom.data.source.Resource
import com.ferdifir.menitcom.domain.model.News
import com.ferdifir.menitcom.domain.usecase.NewsUseCase

class HomeViewModel(private val useCase: NewsUseCase) : ViewModel() {
    fun getTopNews(country: String, category: String): LiveData<Resource<List<News>>> {
        return useCase.getTopHeadlineNews(country, category).asLiveData()
    }
}