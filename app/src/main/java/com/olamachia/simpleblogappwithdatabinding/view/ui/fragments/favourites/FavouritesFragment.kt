package com.olamachia.simpleblogappwithdatabinding.view.ui.fragments.favourites

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.olamachia.simpleblogappwithdatabinding.R
import com.olamachia.simpleblogappwithdatabinding.databinding.FavouritesFragmentBinding
import com.olamachia.simpleblogappwithdatabinding.models.db.FavouritesDatabase
import com.olamachia.simpleblogappwithdatabinding.network.RetrofitInstance
import com.olamachia.simpleblogappwithdatabinding.repository.PostRepository
import com.olamachia.simpleblogappwithdatabinding.utils.ConnectivityLiveData
import com.olamachia.simpleblogappwithdatabinding.view.adapters.AllPostsRecyclerAdapter
import com.olamachia.simpleblogappwithdatabinding.view.ui.activities.MainActivity
import retrofit2.Retrofit

class FavouritesFragment : Fragment(R.layout.favourites_fragment) {

    private lateinit var viewModel: FavouritesViewModel
    private lateinit var binding : FavouritesFragmentBinding
    private lateinit var retrofit: Retrofit
    private lateinit var connectivityLiveData: ConnectivityLiveData
    private lateinit var favouritesAdapter: AllPostsRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FavouritesFragmentBinding.bind(view)

        connectivityLiveData = (activity as MainActivity).connectivityLiveData

        retrofit= RetrofitInstance.instance
        val favouritesDatabase = FavouritesDatabase(requireContext())
        val postRepository = PostRepository(retrofit,favouritesDatabase)
        val favouritesViewModelProviderFactory = FavouritesViewModelProviderFactory(postRepository)
        viewModel = ViewModelProvider(this, favouritesViewModelProviderFactory)[FavouritesViewModel::class.java]

        connectivityLiveData.observe(viewLifecycleOwner,{ networkState->
            if (networkState == true){
                setupRecyclerView()
                viewModel.getFavouritePosts().observe(viewLifecycleOwner,{
                    favouritesAdapter.differ.submitList(it)
                })
            }
        })

        val itemTouchHelperCallback = object :ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val post = favouritesAdapter.differ.currentList[position]
                viewModel.deleteFavourite(post)
                Snackbar.make(view,"Successfully deleted post", Snackbar.LENGTH_LONG).apply {
                        setAction("Undo"){ viewModel.undoDelete(post) }.show()
                    }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply{
            attachToRecyclerView(binding.roomPostsrv) }
    }



    private fun setupRecyclerView(){
        favouritesAdapter = AllPostsRecyclerAdapter("Favourites")
        binding.roomPostsrv.apply {
            adapter = favouritesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}