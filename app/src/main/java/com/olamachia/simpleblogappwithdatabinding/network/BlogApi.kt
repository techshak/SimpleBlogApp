package com.olamachia.simpleblogappwithdatabinding.network

import com.olamachia.simpleblogappwithdatabinding.models.dataclasses.Comments
import com.olamachia.simpleblogappwithdatabinding.models.dataclasses.Post
import retrofit2.Response
import retrofit2.http.*

interface BlogApi {
    @GET("posts")
    suspend fun getBlogPosts(): Response<List<Post>>

    @GET("posts/{id}/comments")
    suspend fun getComments(@Path("id") id:Int): Response<List<Comments>>

    @POST("posts")
    suspend fun sendPost(@Body post: Post): Response<Post>

    @POST("comments")
    suspend fun sendComment(@Body comments: Comments): Response<Comments>

    @GET("posts")
    suspend fun searchPosts(
        @Query("q")searchQuery:String
    ): Response<List<Post>>
}