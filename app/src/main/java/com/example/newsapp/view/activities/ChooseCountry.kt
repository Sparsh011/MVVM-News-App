package com.example.newsapp.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ActivityChooseCountryBinding
import com.example.newsapp.util.Constants.Companion.AUSTRALIAN_FLAG
import com.example.newsapp.util.Constants.Companion.CANADA_FLAG
import com.example.newsapp.util.Constants.Companion.ISRAEL_FLAG
import com.example.newsapp.util.Constants.Companion.INDIAN_FLAG
import com.example.newsapp.util.Constants.Companion.NZ_FLAG
import com.example.newsapp.util.Constants.Companion.RUSSIAN_FLAG
import com.example.newsapp.util.Constants.Companion.SAUDI_FLAG
import com.example.newsapp.util.Constants.Companion.SELECTED_COUNTRY
import com.example.newsapp.util.Constants.Companion.SOUTH_KOREA_FLAG
import com.example.newsapp.util.Constants.Companion.USA_FLAG

class ChooseCountry : AppCompatActivity() {
    private lateinit var mBinding: ActivityChooseCountryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityChooseCountryBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        supportActionBar!!.hide()

        Glide.with(this)
            .load(AUSTRALIAN_FLAG)
            .into(mBinding.flagAustralia)

        Glide.with(this)
            .load(INDIAN_FLAG)
            .into(mBinding.flagIndia)

        Glide.with(this)
            .load(USA_FLAG)
            .into(mBinding.flagUsa)

        Glide.with(this)
            .load(CANADA_FLAG)
            .into(mBinding.flagCanada)

        Glide.with(this)
            .load(ISRAEL_FLAG)
            .into(mBinding.flagIsrael)

        Glide.with(this)
            .load(NZ_FLAG)
            .into(mBinding.flagNZ)

        Glide.with(this)
            .load(RUSSIAN_FLAG)
            .into(mBinding.flagRussia)

        Glide.with(this)
            .load(SAUDI_FLAG)
            .into(mBinding.flagSaudi)

        Glide.with(this)
            .load(SOUTH_KOREA_FLAG)
            .into(mBinding.flagSouthKorea)

        mBinding.flagAustralia.setOnClickListener{
            val intent = Intent(this, NewsActivity::class.java)
            SELECTED_COUNTRY = "au"
            startActivity(intent)
        }

        mBinding.flagIndia.setOnClickListener{
            val intent = Intent(this, NewsActivity::class.java)
            SELECTED_COUNTRY = "in"
            startActivity(intent)
        }

        mBinding.flagUsa.setOnClickListener{
            val intent = Intent(this, NewsActivity::class.java)
            SELECTED_COUNTRY = "us"
            startActivity(intent)
        }

        mBinding.flagNZ.setOnClickListener{
            val intent = Intent(this, NewsActivity::class.java)
            SELECTED_COUNTRY = "nz"
            startActivity(intent)
        }

        mBinding.flagCanada.setOnClickListener{
            val intent = Intent(this, NewsActivity::class.java)
            SELECTED_COUNTRY = "ca"
            startActivity(intent)
        }

        mBinding.flagIsrael.setOnClickListener{
            val intent = Intent(this, NewsActivity::class.java)
            SELECTED_COUNTRY = "il"
            startActivity(intent)
        }

        mBinding.flagSaudi.setOnClickListener{
            val intent = Intent(this, NewsActivity::class.java)
            SELECTED_COUNTRY = "sa"
            startActivity(intent)
        }

        mBinding.flagSouthKorea.setOnClickListener{
            val intent = Intent(this, NewsActivity::class.java)
            SELECTED_COUNTRY = "kr"
            startActivity(intent)
        }

        mBinding.flagRussia.setOnClickListener{
            val intent = Intent(this, NewsActivity::class.java)
            SELECTED_COUNTRY = "ru"
            startActivity(intent)
        }
    }
}