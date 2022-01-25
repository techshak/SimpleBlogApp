package com.olamachia.simpleblogappwithdatabinding.view.ui.fragments.singlepost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olamachia.simpleblogappwithdatabinding.models.dataclasses.Comments
import com.olamachia.simpleblogappwithdatabinding.models.dataclasses.Post
import com.olamachia.simpleblogappwithdatabinding.repository.CommentRepository
import com.olamachia.simpleblogappwithdatabinding.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class SinglePostViewModel(private val commentRepository: CommentRepository) : ViewModel() {

    var commentList: MutableLiveData<Resource<List<Comments>>> = MutableLiveData()
    var sentComment: MutableLiveData<Resource<Comments>> = MutableLiveData()

    //make the call to the repository to get a list of comments
     fun getComments(id:Int)= viewModelScope.launch (Dispatchers.IO){
        commentList.postValue(Resource.Loading())
        val comments = commentRepository.getComments(id)
        commentList.postValue(handleComments(comments))
    }
    //make the call to the repository to send a post
     fun sendComment(comment: Comments)= viewModelScope.launch (Dispatchers.IO) {
        sentComment.postValue(Resource.Loading())
        val sent = commentRepository.sendComment(comment)
        sentComment.postValue(handleSentComment(sent))
    }
    fun isFavouriteCall(id:Int):LiveData<List<Post>>{
        return commentRepository.isFavourite(id)
    }

    fun insertFavourite(post: Post)=viewModelScope.launch (Dispatchers.IO){
        commentRepository.insertFavourite(post)
    }

    fun deleteFavourite(post: Post) = viewModelScope.launch (Dispatchers.IO){
        commentRepository.deleteFavourite(post)
    }

    private fun handleSentComment(sent: Response<Comments>): Resource<Comments>? {

        if(sent.isSuccessful){
            sent.body()?.let{comment->
                return Resource.Success(comment)
            }
        }
        return Resource.Error(sent.message())
    }


    //check to see if the comments were fetched successfully or there was an error
    private fun handleComments(comments: Response<List<Comments>>): Resource<List<Comments>>? {

        if(comments.isSuccessful){
            comments.body()?.let{comment->
                return Resource.Success(comment)
            }
        }
        return Resource.Error(comments.message())
    }

}