package com.olamachia.simpleblogappwithdatabinding.view.ui.fragments.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olamachia.simpleblogappwithdatabinding.models.dataclasses.Post
import com.olamachia.simpleblogappwithdatabinding.repository.PostRepository
import kotlinx.coroutines.launch

class FavouritesViewModel(private val postRepository: PostRepository) : ViewModel() {

    fun getFavouritePosts(): LiveData<List<Post>> {
        return postRepository.getFavourites()
    }
    fun deleteFavourite(post: Post) = viewModelScope.launch {
        postRepository.deleteFavourite(post)
    }
    fun undoDelete(post: Post) = viewModelScope.launch {
        postRepository.insertFavourite(post)
    }
}