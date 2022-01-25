package com.olamachia.simpleblogappwithdatabinding.view.ui.fragments.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.olamachia.simpleblogappwithdatabinding.repository.PostRepository

class FavouritesViewModelProviderFactory(val postRepository: PostRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavouritesViewModel(postRepository) as T
    }
}