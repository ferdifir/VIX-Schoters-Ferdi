package com.ferdifir.menitcom.data.source.pref

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ferdifir.menitcom.data.utils.Const
import com.ferdifir.menitcom.data.utils.Helper.today
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Const.preferencesName)

class SearchPreferences private constructor(private val dataStore: DataStore<Preferences>){

    private val category = stringPreferencesKey(Const.NewsPref.Category.name)
    private val language = stringPreferencesKey(Const.NewsPref.Language.name)
    private val date = stringPreferencesKey(Const.NewsPref.Date.name)
    private val query = stringPreferencesKey(Const.NewsPref.Query.name)

    fun getNewsCategory(): Flow<String> = dataStore.data.map { it[category] ?: Const.sortedBy[1] }
    fun getNewsLanguage(): Flow<String> = dataStore.data.map { it[language] ?: Const.language[2] }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getNewsDate(): Flow<String> = dataStore.data.map { it[date] ?: today.toString() }
    fun getNewsQuery(): Flow<String> = dataStore.data.map { it[query] ?: "Bitcoin" }

    suspend fun saveSortPref(catNews: String) {
        dataStore.edit {
            it[category] = catNews
        }
    }

    suspend fun saveLanguagePref(lang: String) {
        dataStore.edit {
            it[language] = lang
        }
    }

    suspend fun saveDatePref(datepref: String) {
        dataStore.edit {
            it[date] = datepref
        }
    }

    suspend fun saveSearchQuery(qNews: String) {
        dataStore.edit { pref ->
            pref[query] = qNews
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SearchPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SearchPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SearchPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}