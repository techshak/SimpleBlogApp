package com.olamachia.simpleblogappwithdatabinding.view.ui.fragments.addpost

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olamachia.simpleblogappwithdatabinding.models.dataclasses.Post
import com.olamachia.simpleblogappwithdatabinding.repository.PostRepository
import com.olamachia.simpleblogappwithdatabinding.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class AddPostViewModel(private val postRepository: PostRepository) : ViewModel() {

    var sentPost: MutableLiveData<Resource<Post>> = MutableLiveData()

    //make the call to the repository to send a post
    private fun sendPost(post: Post)= viewModelScope.launch (Dispatchers.IO) {
        sentPost.postValue(Resource.Loading())
        val sent = postRepository.sendPost(post)
        sentPost.postValue(handleSentPost(sent))
    }

    private fun handleSentPost(sent: Response<Post>): Resource<Post>? {

        if(sent.isSuccessful){
            sent.body()?.let{sentPost->
                return Resource.Success(sentPost)
            }
        }
        return Resource.Error(sent.message())
    }

    // the function that receives a post and makes the post call
    fun receiveSentPost(post: Post){
        sendPost(post)
    }
}