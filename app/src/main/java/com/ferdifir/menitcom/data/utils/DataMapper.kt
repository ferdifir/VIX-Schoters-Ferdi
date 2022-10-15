package com.ferdifir.menitcom.data.utils

import com.ferdifir.menitcom.data.source.local.entity.NewsEntity
import com.ferdifir.menitcom.data.source.remote.response.ArticlesItem
import com.ferdifir.menitcom.domain.model.News

object DataMapper {
    fun mapTopNewsResponseToEntities(input: List<ArticlesItem>, country: String, category: String): List<NewsEntity> {
        val newsList = ArrayList<NewsEntity>()
        input.map {
            val news = NewsEntity(
                publishedAt = it.publishedAt,
                author = it.author,
                urlToImage = it.urlToImage,
                description = it.description,
                sourceName = it.source?.name,
                title = it.title,
                url = it.url,
                content = it.content,
                isBookmarked = false,
                category = category,
                country = country,
                searchNews = false,
                language = null,
                sortener = null
            )
            newsList.add(news)
        }
        return newsList
    }

    fun mapSearchNewsResponseToEntities(input: List<ArticlesItem>, language: String, sortener: String): List<NewsEntity> {
        val newsList = ArrayList<NewsEntity>()
        input.map {
            val news = NewsEntity(
                publishedAt = it.publishedAt,
                author = it.author,
                urlToImage = it.urlToImage,
                description = it.description,
                sourceName = it.source?.name,
                title = it.title,
                url = it.url,
                content = it.content,
                isBookmarked = false,
                category = null,
                country = null,
                searchNews = true,
                language = language,
                sortener = sortener
            )
            newsList.add(news)
        }
        return newsList
    }

    fun mapEntitiesToDomain(input: List<NewsEntity>): List<News> {
        return input.map {
            News(
                publishedAt = it.publishedAt,
                author = it.author,
                urlToImage = it.urlToImage,
                description = it.description,
                sourceName = it.sourceName,
                title = it.title,
                url = it.url,
                content = it.content,
                isBookmarked = it.isBookmarked,
                category = it.category,
                country = it.country,
                searchNews = it.searchNews,
                language = it.language,
                sortener = it.sortener
            )
        }
    }

    fun mapDomainToEntity(input: News): NewsEntity {
        return NewsEntity(
            publishedAt = input.publishedAt,
            author = input.author,
            urlToImage = input.urlToImage,
            description = input.description,
            sourceName = input.sourceName,
            title = input.title,
            url = input.url,
            content = input.content,
            isBookmarked = input.isBookmarked,
            category = input.category,
            country = input.country,
            searchNews = input.searchNews
        )
    }
}
