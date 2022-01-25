package com.olamachia.simpleblogappwithdatabinding.models.dataclasses

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favourite posts")
@Parcelize
data class Post(
    val body: String?= null,
    @PrimaryKey
    val id: Int? = null,
    val title: String? = null,
    val userId: Int? = null
):Parcelable