package com.arturofilio.instagram_kotlin.activities

import android.os.Bundle
import android.util.Log
import com.arturofilio.instagram_kotlin.R

class SearchActivity : BaseActivity(1) {

    private val TAG = "SearchActivity";
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        Log.d(TAG, "onCreate")
        setupBottomNavigation()
    }
}
