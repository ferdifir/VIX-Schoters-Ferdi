package com.ferdifir.menitcom.presentation.main.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ferdifir.menitcom.data.source.Resource
import com.ferdifir.menitcom.domain.model.News
import com.ferdifir.menitcom.domain.usecase.NewsUseCase
import kotlinx.coroutines.launch

class HomeViewModel(private val useCase: NewsUseCase) : ViewModel() {
    fun getTopNews(country: String, category: String): LiveData<Resource<List<News>>> {
        return useCase.getTopHeadlineNews(country, category).asLiveData()
    }

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