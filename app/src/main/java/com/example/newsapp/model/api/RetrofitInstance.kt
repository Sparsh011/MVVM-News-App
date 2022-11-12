package com.example.newsapp.model.api

import com.example.newsapp.util.Constants.Companion.API_KEY
import com.example.newsapp.util.Constants.Companion.BASE_URL
import com.example.newsapp.util.Constants.Companion.COUNTRY
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    private val api = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(NewsApi::class.java)

    fun getBreakingNews(pageNumber: Int) : Single<NewsResponse>{
        return api.getBreakingNews(COUNTRY, pageNumber, API_KEY)
    }

    fun getSearchNewsResult(searchQuery: String): Single<NewsResponse>{
        return api.searchNews(searchQuery, 1, API_KEY)
    }
}