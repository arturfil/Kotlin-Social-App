package com.arturofilio.instagram_kotlin.models

import com.google.firebase.database.Exclude

data class User(val name: String = "", val username: String = "", val website: String? = null,
                val folows: Map<String, Boolean> = emptyMap(),
                val followers: Map<String, Boolean> = emptyMap(),
                val bio: String? = null, val email: String = "", val phone: Long? = null,
                val photo: String? = null, @Exclude val uid: String? = null)