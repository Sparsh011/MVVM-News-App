package com.example.newsapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.model.api.NewsResponse
import com.example.newsapp.model.api.RetrofitInstance
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class BreakingNewsViewModel: ViewModel() {
    private val retrofitInstance = RetrofitInstance()

    private val compositeDisposable = CompositeDisposable()

    val loadNews = MutableLiveData<Boolean>()
    val newsResponse = MutableLiveData<NewsResponse>()
    val errorLoadingNews = MutableLiveData<Boolean>()

    fun getBreakingNewsFromAPI(){
        loadNews.value = true
        compositeDisposable.add(
            retrofitInstance.getBreakingNews()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<NewsResponse>(){
                    override fun onSuccess(value: NewsResponse) {
                        loadNews.value = false
                        newsResponse.value = value
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

}