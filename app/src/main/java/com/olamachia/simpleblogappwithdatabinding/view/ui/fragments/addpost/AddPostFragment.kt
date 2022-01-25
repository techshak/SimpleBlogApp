package com.olamachia.simpleblogappwithdatabinding.view.ui.fragments.addpost

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.olamachia.simpleblogappwithdatabinding.R
import com.olamachia.simpleblogappwithdatabinding.databinding.AddPostFragmentBinding
import com.olamachia.simpleblogappwithdatabinding.models.dataclasses.Post
import com.olamachia.simpleblogappwithdatabinding.models.dataclasses.PostList
import com.olamachia.simpleblogappwithdatabinding.models.db.FavouritesDatabase
import com.olamachia.simpleblogappwithdatabinding.network.RetrofitInstance
import com.olamachia.simpleblogappwithdatabinding.repository.PostRepository
import com.olamachia.simpleblogappwithdatabinding.utils.Resource
import com.olamachia.simpleblogappwithdatabinding.view.adapters.AllPostsRecyclerAdapter
import retrofit2.Retrofit

class AddPostFragment : Fragment(R.layout.add_post_fragment) {

    private lateinit var viewModel: AddPostViewModel
    private lateinit var retrofit: Retrofit
    private lateinit var binding: AddPostFragmentBinding
    lateinit var progressBar: ProgressBar
    private lateinit var allPostsRecyclerAdapter:AllPostsRecyclerAdapter
    private lateinit var postList: PostList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= AddPostFragmentBinding.bind(view)

        postList = PostList()
        retrofit = RetrofitInstance.instance
        allPostsRecyclerAdapter = AllPostsRecyclerAdapter("AddPost")
        val favouritesDatabase = FavouritesDatabase(requireContext())
        val postRepository = PostRepository(retrofit,favouritesDatabase)
        val addPostViewModelProviderFactory = AddPostViewModelProviderFactory(postRepository)
        viewModel = ViewModelProvider(this,addPostViewModelProviderFactory)[AddPostViewModel::class.java]
        progressBar =  binding.sentPostPb


            binding.sendPostBtn.setOnClickListener {
            val postBody = binding.sentPostBody.text.toString()
            val postTitle = binding.sentPostTitle.text.toString()
            val sentPost = Post(postBody,1,postTitle,12)
            viewModel.receiveSentPost(sentPost)
        }

        viewModel.sentPost.observe(viewLifecycleOwner,{response->
            when(response){
                is Resource.Success->{
                    response.data?.let {
                        binding.sentPostBody.text = null
                        binding.sentPostTitle.text=null
                        postList.completePosts.add(it)
                        allPostsRecyclerAdapter.differ.submitList(postList.completePosts)
                        val directions = AddPostFragmentDirections.actionAddPostToAllPost()
                        findNavController().navigate(directions)
                        progressBar.visibility =View.GONE
                    }
                }
                is Resource.Error->{
                    response.message?.let{
                        progressBar.visibility = View.GONE
                       Toast.makeText(activity,"Fetch error:$it", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading->{ progressBar.visibility = View.VISIBLE}
            }

        })
    }

}