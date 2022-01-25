package com.olamachia.simpleblogappwithdatabinding.view.ui.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.olamachia.simpleblogappwithdatabinding.utils.Connectivity

class MainActivityViewModelProviderFactory(val connectivity: Connectivity):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(connectivity) as T
    }
}