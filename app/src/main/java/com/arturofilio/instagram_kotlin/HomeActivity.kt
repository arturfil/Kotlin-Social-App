package com.arturofilio.instagram_kotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : BaseActivity(0) {

    private val TAG = "HomeActivity"
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Log.d(TAG, "onCreate")

        // setting up the menu nav, otherwise the view doesn't know how to handle the menu layout
        setupBottomNavigation()

        mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()

    }

    override fun onStart() {
        super.onStart()
        if(mAuth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

}
