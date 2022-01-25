package com.olamachia.simpleblogappwithdatabinding.view.ui.fragments.allpost

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olamachia.simpleblogappwithdatabinding.models.dataclasses.Post
import com.olamachia.simpleblogappwithdatabinding.repository.PostRepository
import com.olamachia.simpleblogappwithdatabinding.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class AllPostViewModel(private val postRepository: PostRepository) : ViewModel() {

    var searchedPost: MutableLiveData<Resource<List<Post>>> = MutableLiveData()
    var blogPosts: MutableLiveData<Resource<List<Post>>> = MutableLiveData()

    fun getBlogPosts()=viewModelScope.launch (Dispatchers.IO){
       blogPosts.postValue(Resource.Loading())
        val posts = postRepository.getBlogPosts()
        blogPosts.postValue(handleBlogPosts(posts))
//        Log.i("View Model", posts.body().toString())

    }
    //make the call to the repository to search posts
    fun searchPosts(searchQuery: String) = viewModelScope.launch {
        searchedPost.postValue(Resource.Loading())
        val response = postRepository.searchPost(searchQuery)
        searchedPost.postValue(handleBlogPosts(response))
        //Log.i("Search", response.body().toString())
    }

    private fun handleBlogPosts(posts: Response<List<Post>>): Resource<List<Post>> {
        if (posts.isSuccessful){
            posts.body()?.let{post ->
                return Resource.Success(post)
            }
        }
        return Resource.Error(posts.message())
    }
}