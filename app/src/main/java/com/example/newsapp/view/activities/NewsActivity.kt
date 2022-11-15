package com.example.newsapp.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityNewsBinding
import com.example.newsapp.util.Constants

class NewsActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityNewsBinding
    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        mBinding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        mNavController = findNavController(R.id.newsNavHostFragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.breakingNewsFragment, R.id.searchNewsFragment, R.id.articleFragment, R.id.savedNewsFragment
            )
        )
        setupActionBarWithNavController(mNavController, appBarConfiguration)
        mBinding.bottomNavigationView.setupWithNavController(mNavController)
    }
}