package com.arturofilio.instagram_kotlin.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.arturofilio.instagram_kotlin.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(0) {

    private val TAG = "HomeActivity"

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Log.d(TAG, "onCreate")

        setupBottomNavigation()

        mAuth = FirebaseAuth.getInstance()
//        mAuth.signOut()


        sign_out_text.setOnClickListener {
            mAuth.signOut();
        }
        mAuth.addAuthStateListener {
            if (it.currentUser == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if(mAuth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

}
