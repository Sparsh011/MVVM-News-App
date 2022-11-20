package com.example.newsapp.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentSearchNewsBinding
import com.example.newsapp.model.api.Article
import com.example.newsapp.model.api.NewsResponse
import com.example.newsapp.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.newsapp.view.adapters.NewsAdapter
import com.example.newsapp.viewmodel.NewsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    private lateinit var searchViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private var mBinding: FragmentSearchNewsBinding? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        var job: Job? = null

        mBinding!!.shimmerLayoutSearchNews.stopShimmer()
        mBinding!!.shimmerLayoutSearchNews.visibility = View.INVISIBLE

        mBinding?.etSearch?.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        searchViewModel.safeSearchNewsCall(editable.toString())
                    }
                }
            }
        }

        searchNewsViewModelObserver()
    }

    private fun searchNewsViewModelObserver() {
        searchViewModel.searchNewsResponse.observe(viewLifecycleOwner) { searchNewsResponse ->
            searchNewsResponse?.let {
                mBinding?.rvSearchNews?.visibility = View.VISIBLE
                setNewsResponseInUI(searchNewsResponse)

                mBinding!!.shimmerLayoutSearchNews.stopShimmer()
                mBinding!!.shimmerLayoutSearchNews.visibility = View.GONE

            }
        }

        searchViewModel.loadSearchNews.observe(viewLifecycleOwner) { loadNews ->
            loadNews?.let {
                Log.i("Loading news", "$loadNews")

                mBinding!!.shimmerLayoutSearchNews.visibility = View.VISIBLE
                mBinding!!.shimmerLayoutSearchNews.startShimmer()

            }
        }

        searchViewModel.errorLoadingSearchNews.observe(viewLifecycleOwner) { dataError ->
            dataError?.let {
                Log.i("Fetching news error", "$dataError")
                mBinding!!.shimmerLayoutSearchNews.visibility = View.GONE
                mBinding!!.shimmerLayoutSearchNews.stopShimmer()
            }
        }
    }

    private fun setNewsResponseInUI(searchNewsResponse: NewsResponse) {
        setupRecyclerView(searchNewsResponse.articles)
    }

    private fun setupRecyclerView(articles: List<Article>) {
        newsAdapter = NewsAdapter(this)
        newsAdapter.differ.submitList(articles)
        mBinding?.rvSearchNews.apply {
            this?.adapter = newsAdapter
            this?.layoutManager = LinearLayoutManager(activity)
        }
    }

    fun articleWebView(article: Article) {
        findNavController().navigate(SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(article))
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}