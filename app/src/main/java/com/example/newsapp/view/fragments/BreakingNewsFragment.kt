package com.example.newsapp.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.model.api.Article
import com.example.newsapp.model.api.NewsResponse
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

        breakingNewsViewModel.getBreakingNewsFromAPI()
        breakingNewsViewModelObserver()
    }

    private fun breakingNewsViewModelObserver(){
        breakingNewsViewModel.newsResponse.observe(viewLifecycleOwner){ breakingNewsResponse ->
            breakingNewsResponse?.let {
                mBinding?.rvBreakingNews?.visibility = View.VISIBLE
                setNewsResponseInUI(it)
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
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(this)
        mBinding?.rvBreakingNews.apply {
            this?.adapter = newsAdapter
            this?.layoutManager = LinearLayoutManager(activity)
        }
    }

    fun articleWebView(article: Article){
        findNavController().navigate(BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(article))
    }

    private fun showProgressBar(){
        mBinding?.pbLoadingBreakingNews?.isVisible = true
    }

    private fun hideProgressBar(){
        mBinding?.pbLoadingBreakingNews?.isVisible = false
    }
}