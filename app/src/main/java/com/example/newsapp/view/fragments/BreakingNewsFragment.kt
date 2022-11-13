package com.example.newsapp.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.model.api.Article
import com.example.newsapp.model.api.NewsResponse
import com.example.newsapp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.newsapp.view.adapters.NewsAdapter
import com.example.newsapp.viewmodel.NewsViewModel

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    private lateinit var breakingNewsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    private var mBinding: FragmentBreakingNewsBinding? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        breakingNewsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]


        breakingNewsViewModel.safeBreakingNewsCall()
        breakingNewsViewModelObserver()
    }

    private fun breakingNewsViewModelObserver(){
        breakingNewsViewModel.newsResponse.observe(viewLifecycleOwner){ breakingNewsResponse ->
            breakingNewsResponse?.let {
                mBinding?.rvBreakingNews?.visibility = View.VISIBLE
                setNewsResponseInUI(it)
                val totalPages = it.totalResults / QUERY_PAGE_SIZE + 2
                isLastPage = breakingNewsViewModel.breakingNewsPageNumber == totalPages
                hideProgressBar()
            }
        }

        breakingNewsViewModel.loadNews.observe(viewLifecycleOwner){ loadNews ->
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


    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate){
                breakingNewsViewModel.safeBreakingNewsCall()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }


    private fun setNewsResponseInUI(breakingNewsResponse: NewsResponse) {
        newsAdapter.differ.submitList(breakingNewsResponse.articles.toList())
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(this)
        mBinding?.rvBreakingNews.apply {
            this?.adapter = newsAdapter
            this?.layoutManager = LinearLayoutManager(activity)
            this?.addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }

    fun articleWebView(article: Article){
        findNavController().navigate(BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(article))
    }

    private fun showProgressBar(){
        mBinding?.pbLoadingBreakingNews?.isVisible = true
        isLoading = true
    }

    private fun hideProgressBar(){
        mBinding?.pbLoadingBreakingNews?.isVisible = false
        isLoading = false
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}