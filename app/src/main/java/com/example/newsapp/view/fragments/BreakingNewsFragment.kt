package com.example.newsapp.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.model.api.Article
import com.example.newsapp.model.api.NewsResponse
import com.example.newsapp.view.adapters.NewsAdapter
import com.example.newsapp.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    private lateinit var breakingNewsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        breakingNewsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        breakingNewsViewModel.getBreakingNewsFromAPI()
        breakingNewsViewModelObserver()

//        newsAdapter.setOnItemClickListener {
////            val bundle = Bundle().apply {
////                putSerializable("article", it)
////            }
////
////            findNavController().navigate(
////                R.id.action_breakingNewsFragment_to_articleFragment, bundle
////            )
//        }
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
        newsAdapter.differ.submitList(breakingNewsResponse.articles)
//        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(this)
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

//    fun articleWebView(article: Article){
//        findNavController().navigate(BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(article))
//    }

    private fun showProgressBar(){
        pbLoadingBreakingNews.isVisible = true
    }

    private fun hideProgressBar(){
        pbLoadingBreakingNews.isVisible = false
    }
}