package com.arturofilio.instagram_kotlin.activities

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ValueEventListenerAdapater(val handler: (DataSnapshot) -> Unit): ValueEventListener {
    private val TAG = "ValueEventListenerAdapter"

    override fun onDataChange(data: DataSnapshot) {
        handler(data)
    }

    override fun onCancelled(error: DatabaseError) {
        Log.e("onCancelled", TAG, error.toException())
    }
}

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this,text, duration).show()
}