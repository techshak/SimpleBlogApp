package com.olamachia.simpleblogappwithdatabinding.repository

import androidx.lifecycle.LiveData
import com.olamachia.simpleblogappwithdatabinding.models.dataclasses.Comments
import com.olamachia.simpleblogappwithdatabinding.models.dataclasses.Post
import com.olamachia.simpleblogappwithdatabinding.models.db.FavouritesDatabase
import com.olamachia.simpleblogappwithdatabinding.network.BlogApi
import retrofit2.Retrofit

class CommentRepository(
    retrofit: Retrofit,
    val favouritesDatabase: FavouritesDatabase
    ) {
    private var blogApi: BlogApi = retrofit.create(BlogApi::class.java)

    suspend fun getComments(id:Int) = blogApi.getComments(id)
    suspend fun sendComment(comments: Comments) = blogApi.sendComment(comments)
    suspend fun insertFavourite (post: Post) = favouritesDatabase.getPostDao().upsert(post)
    suspend fun deleteFavourite(post: Post) = favouritesDatabase.getPostDao().deletePost(post)
    fun isFavourite(id:Int): LiveData<List<Post>> = favouritesDatabase.getPostDao().isFavourite(id)
}