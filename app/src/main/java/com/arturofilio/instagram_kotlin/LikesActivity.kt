package com.arturofilio.instagram_kotlin


import android.os.Bundle
import android.util.Log


class LikesActivity : BaseActivity(3) {

    private val TAG = "LikesActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_likes)
        setupBottomNavigation()
        Log.d(TAG, "onCreate")
    }
}
