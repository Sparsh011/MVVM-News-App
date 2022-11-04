package com.example.newsapp.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.model.api.Article
import com.example.newsapp.model.api.NewsResponse
import com.example.newsapp.view.adapters.BreakingNewsAdapter
import com.example.newsapp.viewmodel.BreakingNewsViewModel
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    private lateinit var breakingNewsViewModel: BreakingNewsViewModel
    private lateinit var newsAdapter: BreakingNewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        breakingNewsViewModel = ViewModelProvider(this)[BreakingNewsViewModel::class.java]

        breakingNewsViewModel.getBreakingNewsFromAPI()
        breakingNewsViewModelObserver()
    }

    private fun breakingNewsViewModelObserver(){
        breakingNewsViewModel.newsResponse.observe(viewLifecycleOwner){ breakingNewsResponse ->
            breakingNewsResponse?.let {
                rvBreakingNews.visibility = View.VISIBLE
                setNewsResponseInUI(breakingNewsResponse)
                hideProgressBar()
            }
        }

        breakingNewsViewModel.loadNews.observe(viewLifecycleOwner){
            loadNews ->
            loadNews?.let {
                Log.i("Loading news", "$loadNews")
                showProgressBar()
            }
        }

        breakingNewsViewModel.errorLoadingNews.observe(viewLifecycleOwner){ dataError ->
            dataError?.let {
                Log.i("Fetching news error", "$dataError")
                hideProgressBar()
            }
        }
    }

    private fun setNewsResponseInUI(breakingNewsResponse: NewsResponse) {
        setupRecyclerView(breakingNewsResponse.articles)
    }

    private fun setupRecyclerView(articles: List<Article>) {
        newsAdapter = BreakingNewsAdapter()
        newsAdapter.differ.submitList(articles)
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun showProgressBar(){
        pbLoadingBreakingNews.isVisible = true
    }

    private fun hideProgressBar(){
        pbLoadingBreakingNews.isVisible = false
    }
}