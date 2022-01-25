package com.olamachia.simpleblogappwithdatabinding.models.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.olamachia.simpleblogappwithdatabinding.models.dataclasses.Post

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    //upsert does update if post exists or insert new
    suspend fun upsert (post:Post)

    @Query("SELECT * FROM `favourite posts`")
    fun getAllFavourites():LiveData<List<Post>>

    @Query("SELECT * FROM `favourite posts` WHERE id LIKE :id LIMIT 1")
    fun isFavourite(id:Int):LiveData<List<Post>>

    @Delete
    suspend fun deletePost(post: Post)


}