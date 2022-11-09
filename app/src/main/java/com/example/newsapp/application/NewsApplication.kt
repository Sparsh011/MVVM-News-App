package com.example.newsapp.application

import android.app.Application
import com.example.newsapp.model.database.ArticleDatabase
import com.example.newsapp.model.database.NewsRepository

class NewsApplication : Application(){
    private val database by lazy{
        ArticleDatabase.getDatabase(this@NewsApplication)
    }
    val repository by lazy {
        NewsRepository(database.articleDao())
    }
}