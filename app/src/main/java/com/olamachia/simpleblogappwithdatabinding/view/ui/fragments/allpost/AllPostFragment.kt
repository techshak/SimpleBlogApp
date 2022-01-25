package com.olamachia.simpleblogappwithdatabinding.view.ui.fragments.allpost

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.olamachia.simpleblogappwithdatabinding.R
import com.olamachia.simpleblogappwithdatabinding.databinding.AllPostFragmentBinding
import com.olamachia.simpleblogappwithdatabinding.models.dataclasses.PostList
import com.olamachia.simpleblogappwithdatabinding.models.db.FavouritesDatabase
import com.olamachia.simpleblogappwithdatabinding.network.RetrofitInstance
import com.olamachia.simpleblogappwithdatabinding.repository.PostRepository
import com.olamachia.simpleblogappwithdatabinding.utils.ConnectivityLiveData
import com.olamachia.simpleblogappwithdatabinding.utils.Resource
import com.olamachia.simpleblogappwithdatabinding.view.adapters.AllPostsRecyclerAdapter
import com.olamachia.simpleblogappwithdatabinding.view.ui.activities.MainActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class AllPostFragment : Fragment(R.layout.all_post_fragment) {

    private lateinit var binding :AllPostFragmentBinding
    private lateinit var viewModel: AllPostViewModel
    private lateinit var retrofit: Retrofit
    private lateinit var allPostsRecyclerAdapter: AllPostsRecyclerAdapter
    private lateinit var postList: PostList
    private lateinit var connectivityLiveData: ConnectivityLiveData

    var page = 0
    var isLoading = false
    val limit = 10
    val allPostlayoutManager = LinearLayoutManager(activity)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         binding = AllPostFragmentBinding.bind(view)

        postList = PostList()
        connectivityLiveData = (activity as MainActivity).connectivityLiveData
        retrofit=RetrofitInstance.instance
        val favouritesDatabase = FavouritesDatabase(requireContext())
        val postRepository = PostRepository(retrofit,favouritesDatabase)
        val postViewModelProviderFactory = AllPostViewModelProviderFactory(postRepository)
        viewModel = ViewModelProvider(this, postViewModelProviderFactory)[AllPostViewModel::class.java]


        connectivityLiveData.observe(viewLifecycleOwner,{ networkState->
            if (networkState == true){
               viewModel.getBlogPosts()
            }
        })

        // The job that performs the search operation
        val job: Job?= null
        binding.searchPosts.addTextChangedListener {editable->
            job?.cancel()
            MainScope().launch {
                delay(1500L)
                editable.let {
                    if (editable.toString().isNotEmpty()){
                        // Move back to the first element in the recycler view
                        binding.allPostRv.scrollToPosition(0)
                        viewModel.searchPosts(editable.toString())
                        setupRecyclerView()
                    }
                }
            }
        }

        binding.allPostRv.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy>0){
                    val visibleItemCount = allPostlayoutManager.childCount
                    val pastVisibleItem = allPostlayoutManager.findFirstCompletelyVisibleItemPosition()
                    val total = allPostsRecyclerAdapter.itemCount

                    if (!isLoading){

                        if ((visibleItemCount + pastVisibleItem) >= total){
                            page++
                        }
                    }
                }
            }
        })

        viewModel.searchedPost.observe(viewLifecycleOwner,{response->
            when(response){
                is Resource.Success->{
                    response.data?.let{post ->
                        binding.allPostPb.visibility= View.INVISIBLE
                        // submit the fetched posts to the differ in the adapter
                        allPostsRecyclerAdapter.differ.submitList(post)
                        if (post.isEmpty()){binding.noPosts.visibility = View.VISIBLE}
                    }
                }
                is Resource.Error->{
                    response.message?.let { message->
                        binding.allPostPb.visibility= View.INVISIBLE
                        Log.d("All Posts Fragment", "Fetch error:$message")
                    }
                }
                is Resource.Loading -> {
                    //show the user the progress bar so they know the call is being made
                    binding.allPostPb.visibility= View.VISIBLE
                }
            }
        })
        //Observe the Blog posts live data in the View Model
        viewModel.blogPosts.observe(viewLifecycleOwner, { response->
            when(response){
                is Resource.Success->{
                    response.data?.let{post ->
                        post.forEach {
                            postList.completePosts.add(it)
                        }
                        binding.allPostPb.visibility= View.INVISIBLE
                        setupRecyclerView()
                        // submit the fetched posts to the differ in the adapter
                        allPostsRecyclerAdapter.differ.submitList(postList.completePosts)
                    }
                }
                is Resource.Error->{
                    response.message?.let { message->
                        binding.allPostPb.visibility= View.INVISIBLE
                        Log.d("All Posts Fragment", "Fetch error:$message")
                    }
                }
                is Resource.Loading -> {
                    //show the user the progress bar so they know the call is being made
                    binding.allPostPb.visibility= View.VISIBLE
                }
            }
        })
    }

    private fun setupRecyclerView(){
        allPostsRecyclerAdapter = AllPostsRecyclerAdapter("Home")
        binding.allPostRv.apply {
            adapter = allPostsRecyclerAdapter
            layoutManager = allPostlayoutManager
        }
    }

}

