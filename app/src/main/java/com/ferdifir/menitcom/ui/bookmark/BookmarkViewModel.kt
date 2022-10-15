package com.ferdifir.menitcom.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ferdifir.menitcom.domain.usecase.NewsUseCase

class BookmarkViewModel(useCase: NewsUseCase) : ViewModel() {
    val bookMarkedNews = useCase.getBookmarkedNews().asLiveData()
}