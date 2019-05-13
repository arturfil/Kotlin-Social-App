package com.arturofilio.instagram_kotlin.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.arturofilio.instagram_kotlin.R
import com.arturofilio.instagram_kotlin.utils.FirebaseHelper
import kotlinx.android.synthetic.main.activity_profile_settings.*

class ProfileSettignsActivity : AppCompatActivity() {
    private lateinit var mFirebase: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)

        mFirebase = FirebaseHelper(this)
        sign_out_text.setOnClickListener{mFirebase.auth.signOut()}
        back_image.setOnClickListener { finish() }
    }
}
