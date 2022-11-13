package com.example.newsapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.application.NewsApplication
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.viewmodel.SaveArticleViewModel
import com.example.newsapp.viewmodel.SaveArticleViewModelProviderFactory


class ArticleFragment : Fragment(R.layout.fragment_article) {
    private val args: ArticleFragmentArgs by navArgs()
    private var mBinding: FragmentArticleBinding? = null
    private val mSaveArticleViewModel : SaveArticleViewModel by viewModels {
        SaveArticleViewModelProviderFactory(((requireActivity().application) as NewsApplication).repository)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentArticleBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.let {
            val article = it.article
            mBinding?.webView.apply {
                this?.webViewClient = WebViewClient()
                article.url.let { url ->
                    this?.loadUrl(url.toString())
                }
            }
        }
        mBinding?.fabSaveNews?.setOnClickListener{
            mSaveArticleViewModel.insert(args.article)
            Toast.makeText(context, "Article Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}