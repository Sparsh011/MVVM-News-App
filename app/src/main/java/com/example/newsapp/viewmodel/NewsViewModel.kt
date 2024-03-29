package com.example.newsapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.os.Build.VERSION_CODES.M
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.application.NewsApplication
import com.example.newsapp.model.api.NewsResponse
import com.example.newsapp.model.api.RetrofitInstance
import com.example.newsapp.util.Variables.Companion.SELECTED_COUNTRY
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.IOException

class NewsViewModel(app: Application): AndroidViewModel(app) {
    private val retrofitInstance = RetrofitInstance()
    private val TAG = "NewsViewModel"
    private val compositeDisposable = CompositeDisposable()

//    BreakingNews states -
    val loadNews = MutableLiveData<Boolean>()
    val newsResponse = MutableLiveData<NewsResponse?>()
    val errorLoadingNews = MutableLiveData<Boolean>()

//    Search News states -
    val loadSearchNews = MutableLiveData<Boolean>()
    val searchNewsResponse = MutableLiveData<NewsResponse>()
    val errorLoadingSearchNews = MutableLiveData<Boolean>()

//    Category News States -
    val loadCategoryNews = MutableLiveData<Boolean>()
    val categoryNewsResponse = MutableLiveData<NewsResponse>()
    val errorLoadingCategoryNews = MutableLiveData<Boolean>()

//    Page number for pagination -
    var breakingNewsPageNumber = 1

    private fun getBreakingNewsFromAPI(){
        loadNews.value = true
        compositeDisposable.add(
            retrofitInstance.getBreakingNews(SELECTED_COUNTRY)
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

    private fun searchNews(searchQuery: String){
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

    fun refreshingLayoutBreakingNewsCall(){
        Log.d(TAG, "refreshingLayoutBreakingNewsCall: Here")
        try {
            if (hasInternetConnection()){
                getRefreshedBreakingNewsFromAPI()
            }
            else{
                Log.i("No Internet", "No internet connection")
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> Log.i("IOException", t.message.toString())
                else -> Log.i("Other Exception", t.message.toString())
            }
        }
    }


    private fun getRefreshedBreakingNewsFromAPI(){
        loadNews.value = true
        newsResponse.value = null
        compositeDisposable.add(
            retrofitInstance.getBreakingNews(SELECTED_COUNTRY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<NewsResponse>(){
                    override fun onSuccess(value: NewsResponse) {
                        Log.d(TAG, "onSuccess: $value")
                        newsResponse.value = value
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

    fun safeBreakingNewsCall(){
        try {
            if (hasInternetConnection()){
                getBreakingNewsFromAPI()
            }
            else{
                Log.i("No Internet", "No internet connection")
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> Log.i("IOException", t.message.toString())
                else -> Log.i("Other Exception", t.message.toString())
            }
        }
    }

    fun safeSearchNewsCall(searchQuery: String){
        try {
            if (hasInternetConnection()){
                searchNews(searchQuery)
            }
            else{
                Log.i("No Internet", "No internet connection")
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> Log.i("IOException", t.message.toString())
                else -> Log.i("Other Exception", t.message.toString())
            }
        }
    }

    fun safeCategoryNewsCall(){
        try {
            if (hasInternetConnection()){
                categoryNewsAPICall()
            }
            else{
                Log.i("No Internet", "No internet connection")
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> Log.i("IOException", t.message.toString())
                else -> Log.i("Other Exception", t.message.toString())
            }
        }
    }

    private fun categoryNewsAPICall() {
        loadCategoryNews.value = true
        compositeDisposable.add(
            retrofitInstance.getCategoryNews()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<NewsResponse>(){
                    override fun onSuccess(value: NewsResponse) {
                        categoryNewsResponse.value = value
                        loadCategoryNews.value = false
                        errorLoadingCategoryNews.value = false
                        Log.i("Category response", "$categoryNewsResponse")
                    }

                    override fun onError(e: Throwable) {
                        loadCategoryNews.value = false
                        errorLoadingCategoryNews.value = true
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun hasInternetConnection() : Boolean{
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true

                else -> false
            }
        }
        else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true

                    else -> false
                }
            }
        }

        return false
    }
}