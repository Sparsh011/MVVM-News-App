package com.example.newsapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.application.NewsApplication
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.model.api.Article
import com.example.newsapp.view.adapters.NewsAdapter
import com.example.newsapp.viewmodel.SaveArticleViewModel
import com.example.newsapp.viewmodel.SaveArticleViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
    private var mBinding: FragmentSavedNewsBinding? = null
    private val mSaveArticleViewModel : SaveArticleViewModel by viewModels {
        SaveArticleViewModelProviderFactory(((requireActivity().application) as NewsApplication).repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = NewsAdapter(this)

        mSaveArticleViewModel.savedArticlesList.observe(viewLifecycleOwner){ articles ->

            articles.let {
                mBinding!!.rvSavedNews.layoutManager = LinearLayoutManager(context)
                mBinding!!.rvSavedNews.adapter = adapter

                if (it.isNotEmpty()){
                    mBinding!!.rvSavedNews.visibility = View.VISIBLE
                    mBinding!!.tvNoSavedArticles.visibility = View.GONE
                    adapter.differ.submitList(articles)
                }
            }
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = adapter.differ.currentList[position]
                mSaveArticleViewModel.delete(article)
                if (position == 0){
                    mBinding!!.tvNoSavedArticles.visibility = View.VISIBLE
                }

                Snackbar.make(view, "Article Deleted!", Snackbar.LENGTH_LONG).apply {
                    setAction("UNDO"){
                        mSaveArticleViewModel.insert(article)
                        Snackbar.make(view, "Article Restored!", Snackbar.LENGTH_SHORT).show()
                    }
                }.show()
            }

        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(mBinding!!.rvSavedNews)
        }
    }

    fun articleWebView(article: Article){
        findNavController().navigate(SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(article))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}