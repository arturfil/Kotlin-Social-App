package com.arturofilio.instagram_kotlin.utils

import android.app.Activity
import android.net.Uri
import com.arturofilio.instagram_kotlin.activities.showToast
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class FirebaseHelper(private val activity: Activity) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val database: DatabaseReference = FirebaseDatabase.getInstance()
        .reference
    val storage: StorageReference = FirebaseStorage.getInstance()
        .reference

    fun uploadUserPhoto(photo: Uri, onSuccess: (UploadTask.TaskSnapshot) -> Unit) {
        storage.child("users/${auth.currentUser!!.uid}/photo").putFile(photo)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess(it.result)
                } else {
                    activity.showToast(it.exception!!.message!!)
                }
            }
    }

    fun updateUserPhoto(photoUrl: String, OnSuccess:() -> Unit) {
        database.child("users/${auth.currentUser!!.uid}/photo").setValue(photoUrl)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    OnSuccess();
                } else {
                    activity.showToast(it.exception!!.message!!)
                }
            }
    }

    fun updateUser(updates: Map<String, Any?>, onSuccess: () -> Unit) {
        database.child("users").child(auth.currentUser!!.uid).updateChildren(updates)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    onSuccess()
                } else {
                    activity.showToast(it.exception!!.message!!)
                }
            }
    }

    fun updateEmail(email: String, onSuccess:() -> Unit) {
        auth.currentUser!!.updateEmail(email).addOnCompleteListener {
            if(it.isSuccessful) {
                onSuccess()
            } else {
                activity.showToast(it.exception!!.message!!)
            }
        }
    }

    fun reauthenticate(credential: AuthCredential, onSuccess: () -> Unit) {
        auth.currentUser!!. reauthenticate(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                activity.showToast(it.exception!!.message!!)
            }
        }
    }
    fun currentUserReference(): DatabaseReference = database.child("users").child(auth.currentUser!!.uid)
}