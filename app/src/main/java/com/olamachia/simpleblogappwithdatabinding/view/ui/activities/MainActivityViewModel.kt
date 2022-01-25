package com.olamachia.simpleblogappwithdatabinding.view.ui.activities

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olamachia.simpleblogappwithdatabinding.utils.Connectivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel (private val connectivity: Connectivity):  ViewModel() {

    var connectivityStatus: MutableLiveData<Boolean> = MutableLiveData()
init {
    getConnectivityStatus()
}

    private fun getConnectivityStatus()=viewModelScope.launch (Dispatchers.IO){
        val status = connectivity.hasConnectivity()
        connectivityStatus.postValue(status)
    }

}