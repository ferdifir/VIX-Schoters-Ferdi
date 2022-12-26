package com.ferdifir.menitcom.presentation.main.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ferdifir.menitcom.data.source.Resource
import com.ferdifir.menitcom.domain.model.News
import com.ferdifir.menitcom.domain.usecase.NewsUseCase
import kotlinx.coroutines.launch

class ExploreViewModel(private val useCase: NewsUseCase): ViewModel() {


}