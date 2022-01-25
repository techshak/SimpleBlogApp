package com.olamachia.simpleblogappwithdatabinding.view.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.olamachia.simpleblogappwithdatabinding.R
import com.olamachia.simpleblogappwithdatabinding.databinding.ActivityMainBinding
import com.olamachia.simpleblogappwithdatabinding.utils.ConnectivityLiveData

class MainActivity : AppCompatActivity() {
    lateinit var connectivityLiveData: ConnectivityLiveData
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityLiveData = ConnectivityLiveData(this)
        setTheme(R.style.Theme_SimpleBlogAppWithDataBinding)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connectivityLiveData.observe(this,{ networkState->
             if (networkState == true){
                 binding.networkError.visibility = View.GONE
             }else  binding.networkError.visibility = View.VISIBLE
        })

        val navController = findNavController(R.id.nav_host_fragment_activity_mainactivity)
        binding.bottomNavigationView.setupWithNavController(navController)


    }



}