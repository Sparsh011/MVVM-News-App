package com.example.newsapp.model.api

import com.example.newsapp.util.Constants.Companion.API_KEY
import com.example.newsapp.util.Variables.Companion.SELECTED_COUNTRY
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    fun getBreakingNews(
        @Query("country")
        country: String = SELECTED_COUNTRY,

        @Query("page")
        pageNumber: Int = 1,

        @Query("apiKey")
        apiKey: String = API_KEY,
        ) : Single<NewsResponse>

    @GET("v2/top-headlines")
    fun getCategoryNews(
        @Query("country")
        country: String = SELECTED_COUNTRY,

        @Query("category")
        category: String = "general",

        @Query("page")
        pageNumber: Int = 1,

        @Query("apiKey")
        apiKey: String = API_KEY,
        ) : Single<NewsResponse>



    @GET("v2/everything")
    fun searchNews(
        @Query("q")
        searchQuery: String,

        @Query("page")
        pageNumber: Int = 1,

        @Query("apiKey")
        apiKey: String = API_KEY,
    ) : Single<NewsResponse>
}