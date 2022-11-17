package com.example.newsapp.view.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ActivityChooseCountryBinding
import com.example.newsapp.util.Constants.Companion.AUSTRALIAN_FLAG
import com.example.newsapp.util.Constants.Companion.CANADA_FLAG
import com.example.newsapp.util.Constants.Companion.COUNTRY_NAME_IN_SHARED_PREF
import com.example.newsapp.util.Constants.Companion.ISRAEL_FLAG
import com.example.newsapp.util.Constants.Companion.INDIAN_FLAG
import com.example.newsapp.util.Constants.Companion.NZ_FLAG
import com.example.newsapp.util.Constants.Companion.RUSSIAN_FLAG
import com.example.newsapp.util.Constants.Companion.SAUDI_FLAG
import com.example.newsapp.util.Variables.Companion.SELECTED_COUNTRY
import com.example.newsapp.util.Constants.Companion.SHARED_PREF_FOR_SELECTING_COUNTRY
import com.example.newsapp.util.Constants.Companion.SOUTH_KOREA_FLAG
import com.example.newsapp.util.Constants.Companion.USA_FLAG

class ChooseCountry : AppCompatActivity() {
    private lateinit var mBinding: ActivityChooseCountryBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityChooseCountryBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        supportActionBar!!.hide()


        sharedPref = getSharedPreferences(SHARED_PREF_FOR_SELECTING_COUNTRY, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()


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

        mBinding.flagAustralia.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            SELECTED_COUNTRY = "au"
            startActivity(intent)

            editor.apply {
                putString(COUNTRY_NAME_IN_SHARED_PREF, SELECTED_COUNTRY)
                apply()
            }
        }

        mBinding.flagIndia.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            SELECTED_COUNTRY = "in"
            startActivity(intent)

            editor.apply {
                putString(COUNTRY_NAME_IN_SHARED_PREF, SELECTED_COUNTRY)
                apply()
            }
        }

        mBinding.flagUsa.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            SELECTED_COUNTRY = "us"
            startActivity(intent)

            editor.apply {
                putString(COUNTRY_NAME_IN_SHARED_PREF, SELECTED_COUNTRY)

                apply()
            }
        }

        mBinding.flagNZ.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            SELECTED_COUNTRY = "nz"
            startActivity(intent)

            editor.apply {
                putString(COUNTRY_NAME_IN_SHARED_PREF, SELECTED_COUNTRY)

                apply()
            }
        }

        mBinding.flagCanada.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            SELECTED_COUNTRY = "ca"
            startActivity(intent)

            editor.apply {
                putString(COUNTRY_NAME_IN_SHARED_PREF, SELECTED_COUNTRY)

                apply()
            }
        }

        mBinding.flagIsrael.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            SELECTED_COUNTRY = "il"
            startActivity(intent)

            editor.apply {
                putString(COUNTRY_NAME_IN_SHARED_PREF, SELECTED_COUNTRY)

                apply()
            }
        }

        mBinding.flagSaudi.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            SELECTED_COUNTRY = "sa"
            startActivity(intent)

            editor.apply {
                putString(COUNTRY_NAME_IN_SHARED_PREF, SELECTED_COUNTRY)

                apply()
            }
        }

        mBinding.flagSouthKorea.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            SELECTED_COUNTRY = "kr"
            startActivity(intent)

            editor.apply {
                putString(COUNTRY_NAME_IN_SHARED_PREF, SELECTED_COUNTRY)

                apply()
            }
        }

        mBinding.flagRussia.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            SELECTED_COUNTRY = "ru"
            startActivity(intent)

            editor.apply {
                putString(COUNTRY_NAME_IN_SHARED_PREF, SELECTED_COUNTRY)

                apply()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val prefs = getSharedPreferences(SHARED_PREF_FOR_SELECTING_COUNTRY, MODE_PRIVATE)
        val country = prefs.getString(COUNTRY_NAME_IN_SHARED_PREF, "no")

        if (!country.equals("no")) {
            SELECTED_COUNTRY = country.toString()
            val intent = Intent(this, NewsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }
}









