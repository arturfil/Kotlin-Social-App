package com.arturofilio.instagram_kotlin.models

data class User(val name: String = "", val username: String = "", val website: String? = null,
                val bio: String? = null, val email: String = "", val phone: Long? = null,
                val photo: String = "")