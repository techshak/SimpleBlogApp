package com.olamachia.simpleblogappwithdatabinding.repository

import androidx.lifecycle.LiveData
import com.olamachia.simpleblogappwithdatabinding.models.dataclasses.Post
import com.olamachia.simpleblogappwithdatabinding.models.db.FavouritesDatabase
import com.olamachia.simpleblogappwithdatabinding.network.BlogApi
import retrofit2.Retrofit

class PostRepository (
    retrofit: Retrofit,
    private val favouritesDatabase: FavouritesDatabase
    ){

    private var blogApi: BlogApi = retrofit.create(BlogApi::class.java)
    suspend fun getBlogPosts()=blogApi.getBlogPosts()
    suspend fun sendPost(post: Post)= blogApi.sendPost(post)
    suspend fun searchPost(searchQuery:String)=blogApi.searchPosts(searchQuery)
    suspend fun deleteFavourite(post: Post) = favouritesDatabase.getPostDao().deletePost(post)
    suspend fun insertFavourite (post: Post) = favouritesDatabase.getPostDao().upsert(post)
    fun getFavourites():LiveData<List<Post>> = favouritesDatabase.getPostDao().getAllFavourites()

}