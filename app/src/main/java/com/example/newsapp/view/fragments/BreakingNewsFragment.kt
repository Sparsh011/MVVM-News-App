package com.example.newsapp.view.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AbsListView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.databinding.DialogCustomListBinding
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.model.api.Article
import com.example.newsapp.model.api.NewsResponse
import com.example.newsapp.util.Constants
import com.example.newsapp.util.Constants.Companion.COUNTRY_NAME_IN_SHARED_PREF
import com.example.newsapp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.newsapp.util.Variables.Companion.SELECTED_CATEGORY
import com.example.newsapp.view.activities.ChooseCountry
import com.example.newsapp.view.adapters.CategoryAdapter
import com.example.newsapp.view.adapters.NewsAdapter
import com.example.newsapp.viewmodel.NewsViewModel

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    private lateinit var breakingNewsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var mCustomListDialog: Dialog
    private val TAG = "BreakingNewsFragment"
    private var mBinding: FragmentBreakingNewsBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mCustomListDialog = context?.let { Dialog(it) }!!
        setupRecyclerView()
        breakingNewsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        breakingNewsViewModel.safeBreakingNewsCall()
        breakingNewsViewModelObserver()

        mBinding!!.srlBreakingNews.setOnRefreshListener {

//            TODO - bugs while using SRL
            Log.d(TAG, "Refreshing SRL")

            if (SELECTED_CATEGORY == "NA") {
                breakingNewsViewModel.refreshingLayoutBreakingNewsCall()
                mBinding!!.srlBreakingNews.isRefreshing = false
            } else {
                loadCategoryNews()
                mBinding!!.srlBreakingNews.isRefreshing = false
            }
        }


        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_action_bar, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.changeCountry -> {
                        val sharedPref: SharedPreferences =
                            activity!!.getSharedPreferences(Constants.SHARED_PREF_FOR_SELECTING_COUNTRY, Context.MODE_PRIVATE)
                        val editor = sharedPref.edit()
                        editor.apply {
                            putString(COUNTRY_NAME_IN_SHARED_PREF, "no")
                            apply()
                        }

                        val intent = Intent(context, ChooseCountry::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        requireActivity().finish()
                        return true
                    }

                    R.id.selectCategory -> {
                        categorySelectionDialog()
                    }
                    else -> return false
                }

                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
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
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                breakingNewsViewModel.safeBreakingNewsCall()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }


    private fun categorySelectionDialog() {
        mCustomListDialog = context?.let { Dialog(it) }!!
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(binding.root)

        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = CategoryAdapter(this)
        adapter.categoryDiffer.submitList(Constants.categories())

        binding.rvList.adapter = adapter
        mCustomListDialog.show()
    }

    fun loadCategoryNews() {
        mCustomListDialog.dismiss()
        breakingNewsViewModel.safeCategoryNewsCall()
        categoryNewsViewModelObserver()
    }


//    ViewModel observers - 

    private fun breakingNewsViewModelObserver() {
        breakingNewsViewModel.newsResponse.observe(viewLifecycleOwner) { breakingNewsResponse ->
            breakingNewsResponse?.let {
                mBinding?.rvBreakingNews?.visibility = View.VISIBLE
                setNewsResponseInUI(it)
                val totalPages = it.totalResults / QUERY_PAGE_SIZE + 2
                isLastPage = breakingNewsViewModel.breakingNewsPageNumber == totalPages
                hideProgressBar()
                mBinding!!.shimmerLayout.stopShimmer()
                mBinding!!.shimmerLayout.visibility = View.GONE
            }
        }

        breakingNewsViewModel.loadNews.observe(viewLifecycleOwner) { loadNews ->
            loadNews?.let {
                Log.i("Loading news", "$loadNews")
                showProgressBar()
                mBinding!!.shimmerLayout.startShimmer()
                mBinding!!.shimmerLayout.visibility = View.VISIBLE
            }
        }

        breakingNewsViewModel.errorLoadingNews.observe(viewLifecycleOwner) { dataError ->
            dataError?.let {
                Log.i("Fetching news error", "$dataError")
                hideProgressBar()
                mBinding!!.shimmerLayout.stopShimmer()
                mBinding!!.shimmerLayout.visibility = View.GONE
            }
        }
    }


    private fun categoryNewsViewModelObserver() {
        breakingNewsViewModel.categoryNewsResponse.observe(viewLifecycleOwner) { categoryNewsResponse ->
            categoryNewsResponse?.let {
                mBinding?.rvBreakingNews?.visibility = View.VISIBLE

                setNewsResponseInUI(it)
                hideProgressBar()
                mBinding!!.shimmerLayout.stopShimmer()
                mBinding!!.shimmerLayout.visibility = View.GONE
            }
        }

        breakingNewsViewModel.loadCategoryNews.observe(viewLifecycleOwner) { loadNews ->
            loadNews?.let {
                Log.i("Loading news", "$loadNews")
                showProgressBar()
                mBinding!!.shimmerLayout.startShimmer()
                mBinding!!.shimmerLayout.visibility = View.VISIBLE
            }
        }

        breakingNewsViewModel.errorLoadingCategoryNews.observe(viewLifecycleOwner) { dataError ->
            dataError?.let {
                Log.i("Fetching news error", "$dataError")
                hideProgressBar()
                mBinding!!.shimmerLayout.stopShimmer()
                mBinding!!.shimmerLayout.visibility = View.GONE
            }
        }
    }

    //    Populating response in UI - 
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


    //    Showing and hiding progress bar - 
    private fun showProgressBar() {
//        mBinding?.pbLoadingBreakingNews?.isVisible = true
        isLoading = true
    }

    private fun hideProgressBar() {
//        mBinding?.pbLoadingBreakingNews?.isVisible = false
        isLoading = false
    }

    //    WebView - 
    fun articleWebView(article: Article) {
        findNavController().navigate(BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(article))
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}