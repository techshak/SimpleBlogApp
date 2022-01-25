package com.olamachia.simpleblogappwithdatabinding.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.olamachia.simpleblogappwithdatabinding.R
import com.olamachia.simpleblogappwithdatabinding.models.dataclasses.Post
import com.olamachia.simpleblogappwithdatabinding.view.ui.fragments.allpost.AllPostFragmentDirections
import com.olamachia.simpleblogappwithdatabinding.view.ui.fragments.favourites.FavouritesFragmentDirections

class AllPostsRecyclerAdapter(val currentFragment:String) : RecyclerView.Adapter<AllPostsRecyclerAdapter.AllPostsViewHolder>(){
    private lateinit var postTitle: TextView
    private lateinit var postBody: TextView
    private lateinit var postUserId: TextView

    inner class AllPostsViewHolder(itemView: View, var post: Post?=null): RecyclerView.ViewHolder(itemView) {
        init {
            postTitle = itemView.findViewById(R.id.post_title)
            postBody = itemView.findViewById(R.id.post_body)
            postUserId = itemView.findViewById(R.id.post_user_id)

            // the onclick listener to save and send the current post to safeargs
            itemView.setOnClickListener {
                post?.let{
                    val homeDirections = AllPostFragmentDirections.actionAllPostFragmentToSinglePostFragment(it)
                    val favouritesDirections = FavouritesFragmentDirections.actionFavouritesToSinglePostFragment(it)
                    if (currentFragment == "Home"){
                           itemView.findNavController().navigate(homeDirections)
                    } else if(currentFragment == "Favourites"){
                        itemView.findNavController().navigate(favouritesDirections)
                    }

                }
            }
        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<Post>(){
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllPostsViewHolder {
        return AllPostsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_post_item,parent,false))
    }

    override fun onBindViewHolder(holder: AllPostsViewHolder, position: Int) {

        val selectedPost = differ.currentList[position]
        val userid ="User: "+selectedPost.userId.toString()
        holder.itemView.apply {
            postTitle.text = selectedPost.title
            postBody.text = selectedPost.body
            postUserId.text = userid
        }
        holder.post = selectedPost
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}