package com.example.newsapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.model.api.NewsResponse
import com.example.newsapp.model.api.RetrofitInstance
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class NewsViewModel: ViewModel() {
    private val retrofitInstance = RetrofitInstance()

    private val compositeDisposable = CompositeDisposable()

//    BreakingNews states -
    val loadNews = MutableLiveData<Boolean>()
    val newsResponse = MutableLiveData<NewsResponse>()
    val errorLoadingNews = MutableLiveData<Boolean>()

//    Search News states -
    val loadSearchNews = MutableLiveData<Boolean>()
    val searchNewsResponse = MutableLiveData<NewsResponse>()
    val errorLoadingSearchNews = MutableLiveData<Boolean>()

//    Page numbers -
    var breakingNewsPageNumber = 1

    fun getBreakingNewsFromAPI(){
        loadNews.value = true
        compositeDisposable.add(
            retrofitInstance.getBreakingNews(breakingNewsPageNumber)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<NewsResponse>(){
                    override fun onSuccess(value: NewsResponse) {
                        if (newsResponse.value == null){
                            newsResponse.value = value
                        }
                        else{
                            val oldArticles = newsResponse.value!!.articles
                            val newArticles = value.articles
                            oldArticles.addAll(newArticles)
                            val newNewsResponse = NewsResponse(oldArticles, newsResponse.value!!.status, oldArticles.size)
                            newsResponse.value = newNewsResponse
                        }
                        breakingNewsPageNumber++
                        loadNews.value = false
                        errorLoadingNews.value = false
                    }

                    override fun onError(e: Throwable) {
                        loadNews.value = false
                        errorLoadingNews.value = true
                        e.printStackTrace()
                    }
                })
        )
    }

    fun searchNews(searchQuery: String){
        loadSearchNews.value = true
        compositeDisposable.add(
            retrofitInstance.getSearchNewsResult(searchQuery)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<NewsResponse>(){
                    override fun onSuccess(value: NewsResponse) {
                        searchNewsResponse.value = value
                        loadSearchNews.value = false
                        errorLoadingSearchNews.value = false
                    }

                    override fun onError(e: Throwable) {
                        loadSearchNews.value = false
                        errorLoadingSearchNews.value = true
                        e.printStackTrace()
                    }
                })
        )
    }
}