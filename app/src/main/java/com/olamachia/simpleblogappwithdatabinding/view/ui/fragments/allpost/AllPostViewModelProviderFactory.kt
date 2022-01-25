package com.olamachia.simpleblogappwithdatabinding.view.ui.fragments.allpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.olamachia.simpleblogappwithdatabinding.repository.PostRepository

class AllPostViewModelProviderFactory(val postRepository: PostRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllPostViewModel(postRepository) as T
    }
}