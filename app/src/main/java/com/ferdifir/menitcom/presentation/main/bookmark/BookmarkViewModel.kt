package com.ferdifir.menitcom.presentation.main.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ferdifir.menitcom.domain.usecase.NewsUseCase

class BookmarkViewModel(useCase: NewsUseCase) : ViewModel() {
    val bookMarkedNews = useCase.getBookmarkedNews().asLiveData()
}