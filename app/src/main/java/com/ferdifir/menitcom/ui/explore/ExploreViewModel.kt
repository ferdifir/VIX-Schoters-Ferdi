package com.ferdifir.menitcom.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ferdifir.menitcom.data.source.Resource
import com.ferdifir.menitcom.domain.model.News
import com.ferdifir.menitcom.domain.usecase.NewsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ExploreViewModel(private val useCase: NewsUseCase): ViewModel() {

    fun getSearchNews(): LiveData<Resource<List<News>>> {
        return useCase.getSearchNews().asLiveData()
    }

    fun saveSortPreferences(sortener: String) {
        viewModelScope.launch { useCase.saveSortPreferences(sortener) }
    }

    fun saveLanguagePreferences(language: String) {
        viewModelScope.launch { useCase.saveLanguagePreferences(language) }
    }

    fun saveDatePreferences(date: String) {
        viewModelScope.launch { useCase.saveDatePreferences(date) }
    }

    fun saveQueryPreferences(query: String) {
        viewModelScope.launch { useCase.saveQueryPreferences(query) }
    }
}