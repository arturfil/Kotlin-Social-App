package com.arturofilio.instagram_kotlin.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.arturofilio.instagram_kotlin.R
import com.arturofilio.instagram_kotlin.utils.FirebaseHelper
import com.arturofilio.instagram_kotlin.utils.GlideApp
import com.arturofilio.instagram_kotlin.utils.ValueEventListenerAdapater
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.feed_item.view.*

class HomeActivity : BaseActivity(0) {
    private val TAG = "HomeActivity"
    private lateinit var mFirebase: FirebaseHelper

    override fun onCreate(savedInstanceState:  Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Log.d(TAG, "onCreate")
        setupBottomNavigation()

        mFirebase = FirebaseHelper(this)
        mFirebase.auth.addAuthStateListener {
            if (it.currentUser == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mFirebase.auth.currentUser
        if(currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            mFirebase.database.child("feed-post").child(currentUser.uid)
                .addValueEventListener(ValueEventListenerAdapater{
                    val posts = it.children.map{ it.getValue(FeedPost::class.java)!!}
                    Log.d(TAG, "feedPosts: ${posts.joinToString("\n", "\n") }}")
                    feed_recycler.adapter = FeedAdapter(posts)
                    feed_recycler.layoutManager = LinearLayoutManager(this)
            })
        }
    }

}

class FeedAdapter(private val posts: List<FeedPost>): RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    class  ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout
            .feed_item,parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, positition: Int) {
        holder.view.post_image.loadImage(posts[positition].image)
    }

    private fun ImageView.loadImage(image: String) {
        GlideApp.with(this).load(image).centerCrop().into(this)
    }

    override fun getItemCount() = posts.size
}













