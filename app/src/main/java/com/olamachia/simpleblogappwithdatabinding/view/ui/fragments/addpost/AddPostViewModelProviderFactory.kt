package com.olamachia.simpleblogappwithdatabinding.view.ui.fragments.addpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.olamachia.simpleblogappwithdatabinding.repository.PostRepository

class AddPostViewModelProviderFactory(private val postRepository: PostRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddPostViewModel(postRepository) as T
    }
}