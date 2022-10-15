package com.ferdifir.menitcom.data.utils

object Const {

    const val preferencesName = "News"

    enum class NewsPref {
        Category, Language, Date, Query
    }

    val category = arrayOf(
        "business",
        "entertainment",
        "general",
        "health",
        "science",
        "sports",
        "technology"
    )
    val language = arrayOf(
        "ar", "de", "en", "es", "fr", "he", "it", "nl", "no", "pt", "ru", "sv", "ud", "zh"
    )
    val languageAdapter = arrayOf(
        "Arabic", "German", "English", "French", "Hebrew", "Italian", "Dutch", "Norwegian", "Portugeese", "Russian", "Swedish", "Udmurt", "Chinese"
    )
    val sortedBy = arrayOf(
        "relevancy", "popularity", "publishedAt"
    )
    val sortener = arrayOf(
        "Relevance", "Popularity", "Publish Date"
    )
    val date = arrayOf(
        "Today", "This week", "This month"
    )

    const val SECTION_NUMBER = "Section Nummber"
    const val SEARCH_FOKUS = "Focus"
    const val EXTRA_NEWS = "Extra News"
    const val COPY_TO_CLIPBOARD = "Copy to clipboard"

    val myGithub = "https://github.com/ferdifir"
    val myLinkedin = "https://www.linkedin.com/in/ferdifirmansyah/"
    val myInstagram = "https://www.instagram.com/ferdi.doc/"
}