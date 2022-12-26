package com.ferdifir.menitcom.data.utils

object Const {

    const val preferencesName = "News"

    enum class NewsPref {
        Category, Language, Date, Query
    }

    val category = arrayOf(
        "general",
        "business",
        "entertainment",
        "health",
        "science",
        "sports",
        "technology"
    )
    val language = arrayOf(
        "en", "fr", "it", "nl", "ru"
    )
    val languageAdapter = arrayOf(
        "English", "French", "Italian", "Dutch", "Russian"
    )
    val lang = mapOf(
        "English" to "en", "French" to "fr", "Italian" to "it", "Dutch" to "nl", "Russian" to "ru"
    )
    val sortedBy = arrayOf(
        "relevancy", "popularity", "publishedAt"
    )
    val sortener = arrayOf(
        "Relevance", "Popularity", "Publish Date"
    )
    val sorter = mapOf(
        "Relevance" to "relevancy", "Popularity" to "popularity", "Publish Date" to "publishedAt"
    )
    val date = arrayOf(
        "Today", "This week", "This month"
    )

    const val SECTION_NUMBER = "Section Nummber"
    const val SEARCH_FOKUS = "Focus"
    const val EXTRA_NEWS = "Extra News"
    const val COPY_TO_CLIPBOARD = "Copy to clipboard"

    const val myGithub = "https://github.com/ferdifir"
    const val myLinkedin = "https://www.linkedin.com/in/ferdifirmansyah/"
    const val myInstagram = "https://www.instagram.com/ferdi.doc/"
}