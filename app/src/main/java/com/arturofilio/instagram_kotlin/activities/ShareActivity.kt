package com.arturofilio.instagram_kotlin.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.arturofilio.instagram_kotlin.R
import com.arturofilio.instagram_kotlin.utils.CameraHelper
import com.arturofilio.instagram_kotlin.utils.GlideApp
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : BaseActivity(2) {
    private val TAG = "ShareActiviy"
    private lateinit var mCameraHelper: CameraHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        Log.d(TAG, "onCreate")

        mCameraHelper = CameraHelper(this)
        mCameraHelper.takeCameraPicture()

        back_image.setOnClickListener{finish()}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == mCameraHelper.REQUEST_CODE && resultCode == RESULT_OK) {
            GlideApp.with(this).load(mCameraHelper.imageUri).centerCrop().into(post_image)
        }
   }
}
