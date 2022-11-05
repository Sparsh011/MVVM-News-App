package com.example.newsapp.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        newsAdapter.setOnItemClickListener {
//            val bundle = Bundle().apply {
//                putSerializable("article", it)
//            }
//
//            findNavController().navigate(
//                R.id.action_searchNewsFragment_to_articleFragment, bundle
//            )
//        }
    }
}