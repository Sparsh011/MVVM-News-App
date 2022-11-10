package com.example.newsapp.viewmodel

import androidx.lifecycle.*
import com.example.newsapp.model.api.Article
import com.example.newsapp.model.database.NewsRepository
import kotlinx.coroutines.launch

class SaveArticleViewModel(private val repository: NewsRepository): ViewModel() {
    fun insert(article: Article) = viewModelScope.launch {
        repository.addArticle(article)
    }

    val savedArticlesList: LiveData<List<Article>> = repository.savedArticles.asLiveData()


    fun delete(article: Article) = viewModelScope.launch{
        repository.deleteArticle(article)
    }
}

class SaveArticleViewModelProviderFactory(private val repository: NewsRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SaveArticleViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SaveArticleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}