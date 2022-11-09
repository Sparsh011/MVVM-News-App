package com.example.newsapp.model.database

import androidx.annotation.WorkerThread
import androidx.room.Delete
import com.example.newsapp.model.api.Article
import kotlinx.coroutines.flow.Flow

class NewsRepository(
    private val articleDAO: ArticleDAO
) {

    @WorkerThread
    suspend fun addArticle(article: Article){
        articleDAO.updateOrInsertArticle(article)
    }


    @WorkerThread
    suspend fun deleteArticle(article: Article){
        articleDAO.deleteArticle(article)
    }


    val savedArticles: Flow<List<Article>> = articleDAO.retrieveSavedArticles()

}