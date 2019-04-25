package com.arturofilio.instagram_kotlin.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.text.Editable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.arturofilio.instagram_kotlin.R
import com.arturofilio.instagram_kotlin.models.User
import com.arturofilio.instagram_kotlin.views.PasswordDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : AppCompatActivity(), PasswordDialog.Listener {

    private val TAG = "EditProfileActivity"
    private lateinit var mUser: User
    private lateinit var mPendingUser: User
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private lateinit var mStorage: StorageReference
    private val TAKE_PICTURE_REQUEST_CODE = 1
    val simpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
    private lateinit var mImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        Log.d(TAG, "onCreate")

        close_image.setOnClickListener {
            finish()
        }

        save_image.setOnClickListener{ updateProfile() }

        change_photo_text.setOnClickListener{ takeCameraPicture()}

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mStorage = FirebaseStorage.getInstance().reference

        mDatabase.child("users").child(mAuth.currentUser!!.uid)
            .addListenerForSingleValueEvent(ValueEventListenerAdapater {
            mUser = it.getValue(User::class.java)!!
            name_input.setText(mUser!!.name, TextView.BufferType.EDITABLE)
            username_input.setText(mUser.username, TextView.BufferType.EDITABLE)
            website_input.setText(mUser.website, TextView.BufferType.EDITABLE)
            bio_input.setText(mUser.bio, TextView.BufferType.EDITABLE)
            email_input.setText(mUser.email, TextView.BufferType.EDITABLE)
            phone_input.setText(mUser.phone?.toString(), TextView.BufferType.EDITABLE)
            profile_image.loadUserPhoto(mUser.photo)
        })
    }

    private fun takeCameraPicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null ) {
            val imageFile = createImageFile()
            mImageUri = FileProvider.getUriForFile(this,
                "com.arturofilio.instagram_kotlin.fileprovider",
                imageFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
            startActivityForResult(intent, TAKE_PICTURE_REQUEST_CODE)
        }
    }

    private fun createImageFile(): File {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${simpleDateFormat.format(Date())}_",
            ".jpg",
            storageDir
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TAKE_PICTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            val uid = mAuth.currentUser!!.uid
            mStorage.child("users/$uid/photo").putFile(mImageUri)
                .addOnCompleteListener{
                if (it.isSuccessful) {
                    val photoUrl = it.result.downloadUrl.toString()
                    mDatabase.child("users/$uid/photo").setValue(photoUrl)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                mUser = mUser.copy(photo = photoUrl)
                                profile_image.loadUserPhoto(mUser.photo)
                            } else {
                                showToast(it.exception!!.message!!)
                            }
                        }
                } else {
                    showToast(it.exception!!.message!!)
                }
            }
        }
    }
                                                                                  
    private fun updateProfile() {
        mPendingUser = User(
            name = name_input.text.toString(),
            username = username_input.text.toString(),
            website = website_input.text.toString(),
            bio = bio_input.text.toString(),
            email = email_input.text.toString(),
            phone = phone_input.text.toString().toLong()

        )
        var error = validate(mPendingUser)
        if (error == null) {
            if (mPendingUser.email == mUser.email) {
                updateUser(mPendingUser)
            } else {
                PasswordDialog().show(supportFragmentManager, "password_dialog")
            }
        } else {
            showToast(error)
        }
    }

    private fun readInputs(): User {
        return User(
            name = name_input.text.toString(),
            username = username_input.text.toString(),
            website = website_input.text.toString(),
             email = email_input.text.toString(),
            bio = bio_input.text.toStringOrNull(),
            phone = phone_input.text.toString().toLongOrNull()
        )
    }

    override fun onPasswordConfirm(password: String) {
        val credential = EmailAuthProvider.getCredential(mUser.email, password)
        mAuth.currentUser!!.reauthenticate(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                mAuth.currentUser!!.updateEmail(mPendingUser.email).addOnCompleteListener {
                    if (it.isSuccessful) {
                        updateUser(mPendingUser)
                    }
                }
            } else {
                showToast(it.exception!!.message!!)
            }
        }
    }

    private fun updateUser(user:User) {
        val updatesMap = mutableMapOf<String,Any?>()
        if(user.name != mUser.name) updatesMap["name"] = user.name
        if(user.username != mUser.username) updatesMap["username"] = user.username
        if(user.website != mUser.website) updatesMap["website"] = user.website
        if(user.bio != mUser.bio) updatesMap["bio"] = user.bio
        if(user.email != mUser.email) updatesMap["email"] = user.email
        if(user.phone != mUser.phone) updatesMap["phone"] = user.phone

        mDatabase.child("users").child(mAuth.currentUser!!.uid).updateChildren(updatesMap)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast("Profile saved")
                    finish()
                } else {
                    showToast(it.exception!!.message!!)
                }
            }
    }



    private fun validate(user:User): String? =
        when {
            user.name.isEmpty() -> "Please enter name"
            user.username.isEmpty() -> "Please enter name"
            user.email.isEmpty() -> "Please enter name"
            else -> null
        }

}