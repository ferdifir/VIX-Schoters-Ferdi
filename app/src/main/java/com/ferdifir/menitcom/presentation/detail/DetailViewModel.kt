package com.ferdifir.menitcom.presentation.detail

import androidx.lifecycle.ViewModel
import com.ferdifir.menitcom.domain.model.News
import com.ferdifir.menitcom.domain.usecase.NewsUseCase

class DetailViewModel(private val useCase: NewsUseCase): ViewModel() {
    fun setBookmarkedNews(news: News, state: Boolean) {
        return useCase.setBookmarkedNews(news, state)
    }
}