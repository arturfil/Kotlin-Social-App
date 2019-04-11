package com.arturofilio.instagram_kotlin

import android.os.Bundle
import android.util.Log

class ShareActivity : BaseActivity(2) {

    private val TAG = "ShareActiviy"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        setupBottomNavigation()
        Log.d(TAG, "onCreate")
    }
}
