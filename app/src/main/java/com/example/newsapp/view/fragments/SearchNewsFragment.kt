package com.example.newsapp.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.model.api.Article
import com.example.newsapp.model.api.NewsResponse
import com.example.newsapp.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.newsapp.view.adapters.NewsAdapter
import com.example.newsapp.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    private lateinit var searchViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    val TAG = "SearchNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel = ViewModelProvider(this)[NewsViewModel::class.java]

//        newsAdapter.setOnItemClickListener {
//            val bundle = Bundle().apply {
//                putSerializable("article", it)
//            }
//
//            findNavController().navigate(
//                R.id.action_searchNewsFragment_to_articleFragment, bundle
//            )
//        }

        var job: Job? = null

        etSearch.addTextChangedListener{ editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                        searchViewModel.searchNews(editable.toString())
                    }
                }
            }
        }

        searchNewsViewModelObserver()
    }

    private fun searchNewsViewModelObserver(){
        searchViewModel.searchNewsResponse.observe(viewLifecycleOwner){ searchNewsResponse ->
            searchNewsResponse?.let {
                rvSearchNews.visibility = View.VISIBLE
                setNewsResponseInUI(searchNewsResponse)
                hideProgressBar()
            }
        }

        searchViewModel.loadSearchNews.observe(viewLifecycleOwner){ loadNews ->
            loadNews?.let {
                Log.i("Loading news", "$loadNews")
                showProgressBar()
            }
        }

        searchViewModel.errorLoadingSearchNews.observe(viewLifecycleOwner){ dataError ->
            dataError?.let {
                Log.i("Fetching news error", "$dataError")
                hideProgressBar()
            }
        }
    }

    private fun setNewsResponseInUI(searchNewsResponse: NewsResponse) {
        setupRecyclerView(searchNewsResponse.articles)
    }

    private fun setupRecyclerView(articles: List<Article>) {
        newsAdapter = NewsAdapter(this)
        newsAdapter.differ.submitList(articles)
        rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun showProgressBar(){
        pbLoadingSearchNews.isVisible = true
    }

    private fun hideProgressBar(){
        pbLoadingSearchNews.isVisible = false
    }
}