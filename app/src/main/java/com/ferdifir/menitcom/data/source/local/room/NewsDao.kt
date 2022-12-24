package com.ferdifir.menitcom.data.source.local.room

import androidx.room.*
import com.ferdifir.menitcom.data.source.local.entity.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Query("SELECT * FROM news where isBookmarked = 1")
    fun getBookmarkedNews(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news WHERE category = :category AND country = :country AND searchNews = 0")
    fun getTopNews(category: String, country: String): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news WHERE searchNews = 1 " +
            "AND sortener = :sortedBy AND language = :language " +
            "AND publishedAt >= :startDate AND publishedAt <= :finalDate " +
            "AND title LIKE '%' || :query || '%' " +
            "OR description LIKE '%' || :query || '%' " +
            "OR content LIKE '%' || :query || '%'")
    fun getSearchNews(query: String, sortedBy: String, startDate: String, finalDate: String, language: String):
            Flow<List<NewsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: List<NewsEntity>)

    @Update
    fun updateNews(news: NewsEntity)

    @Query("DELETE FROM news")
    fun deleteAll()
}