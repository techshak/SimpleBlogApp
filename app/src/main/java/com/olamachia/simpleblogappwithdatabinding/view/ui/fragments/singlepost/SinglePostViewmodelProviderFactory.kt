package com.olamachia.simpleblogappwithdatabinding.view.ui.fragments.singlepost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.olamachia.simpleblogappwithdatabinding.repository.CommentRepository

class SinglePostViewmodelProviderFactory(val commentRepository:CommentRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SinglePostViewModel(commentRepository) as T
    }
}