package com.example.newsapp.model.api

import com.example.newsapp.util.Constants.Companion.API_KEY
import com.example.newsapp.util.Constants.Companion.BASE_URL
import com.example.newsapp.util.Variables.Companion.SELECTED_CATEGORY
import com.example.newsapp.util.Variables.Companion.SELECTED_COUNTRY
import io.reactivex.rxjava3.core.Single
import okhttp3.Cache
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.net.InetAddress
import java.net.Proxy
import java.util.concurrent.TimeUnit

class RetrofitInstance {

    private val appCache = Cache(File("cacheDir", "okhttpcache"), 10 * 1024 * 1024)

    private val bootstrapClient = OkHttpClient.Builder()
        .cache(appCache)
        .proxy(Proxy.NO_PROXY)
        .build()

    private val dns = DnsOverHttps.Builder()
        .client(bootstrapClient)
        .url("https://dns.google/dns-query".toHttpUrl())
        .bootstrapDnsHosts(InetAddress.getByName("8.8.4.4"), InetAddress.getByName("8.8.8.8"))
        .build()

    private val okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .dns(dns)
        .proxy(Proxy.NO_PROXY)
        .build()

    private val api = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(okHttpClient)
        .build()
        .create(NewsApi::class.java)

    fun getBreakingNews(country: String, pageNumber: Int) : Single<NewsResponse>{
        return api.getBreakingNews(country, pageNumber, API_KEY)
    }

    fun getSearchNewsResult(searchQuery: String): Single<NewsResponse>{
        return api.searchNews(searchQuery, 1, API_KEY)
    }

    fun getCategoryNews() : Single<NewsResponse>{
        return api.getCategoryNews(SELECTED_COUNTRY, SELECTED_CATEGORY, 1, API_KEY)
    }
}