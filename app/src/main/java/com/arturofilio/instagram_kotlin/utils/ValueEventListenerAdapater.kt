package com.arturofilio.instagram_kotlin.utils

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ValueEventListenerAdapater(val handler: (DataSnapshot) -> Unit):
    ValueEventListener {
    private val TAG = "ValueEventListenerAdapter"

    override fun onDataChange(data: DataSnapshot) {
        handler(data)
    }

    override fun onCancelled(error: DatabaseError) {
        Log.e("onCancelled", TAG, error.toException())
    }
}