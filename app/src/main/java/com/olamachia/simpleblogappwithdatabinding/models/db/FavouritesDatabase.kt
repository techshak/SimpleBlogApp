package com.olamachia.simpleblogappwithdatabinding.models.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.olamachia.simpleblogappwithdatabinding.models.dataclasses.Post

@Database(
    entities = [Post::class],
    version = 1
)
abstract class FavouritesDatabase:RoomDatabase() {

    abstract fun getPostDao():PostDao

    companion object{
        //It is volatile so all threads will be notified when one thread makes changes to it
        @Volatile
        private var instance: FavouritesDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                FavouritesDatabase::class.java,
                "favourite_posts_db.db"
            ).build()
    }

}