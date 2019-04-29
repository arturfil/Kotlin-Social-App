package com.arturofilio.instagram_kotlin.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.arturofilio.instagram_kotlin.R
import com.arturofilio.instagram_kotlin.R.drawable.profile_image
import com.arturofilio.instagram_kotlin.models.User
import com.arturofilio.instagram_kotlin.utils.FirebaseHelper
import com.arturofilio.instagram_kotlin.utils.ValueEventListenerAdapater
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity(4) {

    private val TAG = "ProfileActivity"
    private lateinit var mFirebaseHelper: FirebaseHelper
    private lateinit var mUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupBottomNavigation()
        Log.d(TAG, "onCreate")

        edit_profile_btn.setOnClickListener{
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        mFirebaseHelper = FirebaseHelper(this)
        mFirebaseHelper.currentUserReference().addValueEventListener(ValueEventListenerAdapater{

            mUser = it.getValue(User::class.java)!!
            profile_pic.loadUserPhoto(mUser.photo)
            username_text.text = mUser.username
        })
    }
}
