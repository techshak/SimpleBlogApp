package com.olamachia.simpleblogappwithdatabinding.view.ui.fragments.singlepost

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.olamachia.simpleblogappwithdatabinding.R
import com.olamachia.simpleblogappwithdatabinding.databinding.SinglePostFragmentBinding
import com.olamachia.simpleblogappwithdatabinding.models.dataclasses.Comments
import com.olamachia.simpleblogappwithdatabinding.models.dataclasses.Post
import com.olamachia.simpleblogappwithdatabinding.models.db.FavouritesDatabase
import com.olamachia.simpleblogappwithdatabinding.network.RetrofitInstance
import com.olamachia.simpleblogappwithdatabinding.repository.CommentRepository
import com.olamachia.simpleblogappwithdatabinding.utils.ConnectivityLiveData
import com.olamachia.simpleblogappwithdatabinding.utils.Resource
import com.olamachia.simpleblogappwithdatabinding.view.adapters.CommentsRecyclerAdapter
import com.olamachia.simpleblogappwithdatabinding.view.ui.activities.MainActivity
import retrofit2.Retrofit

class SinglePostFragment : Fragment(R.layout.single_post_fragment) {

    private lateinit var binding: SinglePostFragmentBinding
    private val args: SinglePostFragmentArgs by navArgs()
    private lateinit var retrofit: Retrofit
    private lateinit var commentsRecyclerAdapter: CommentsRecyclerAdapter
    private lateinit var passedPost: Post
    private lateinit var comment: EditText
    private lateinit var viewModel: SinglePostViewModel
    private lateinit var connectivityLiveData: ConnectivityLiveData
    var likeStatus:Boolean = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SinglePostFragmentBinding.bind(view)

        retrofit= RetrofitInstance.instance
        val favouritesDatabase = FavouritesDatabase(requireContext())
        val commentRepository =CommentRepository(retrofit,favouritesDatabase)
        connectivityLiveData = (activity as MainActivity).connectivityLiveData
        val singlePostViewModelProviderFactory =SinglePostViewmodelProviderFactory(commentRepository)
        viewModel = ViewModelProvider(this,singlePostViewModelProviderFactory)[SinglePostViewModel::class.java]

        //save the post from safeargs to a variable
        passedPost=args.post
        val compiledComments:MutableList<Comments> = mutableListOf()

        //Initialize the views declared as lateInit
        comment = binding.addCommentEt

        //Set the text from safeargs to text views
        binding.singlePostTitle.text = passedPost.title
        binding.singlePostBody.text = passedPost.body

        passedPost.id?.let { viewModel.isFavouriteCall(it).observe (viewLifecycleOwner,{favouriteList->
                if(favouriteList.isEmpty())binding.likePostIv.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
                else {
                    binding.likePostIv.setColorFilter(ContextCompat.getColor(requireContext(), R.color.oxblood))
                    likeStatus = true
                }

            binding.likePostIv.setOnClickListener {
                if (favouriteList.isEmpty()){
                    viewModel.insertFavourite(passedPost)
                    binding.likePostIv.setColorFilter(ContextCompat.getColor(requireContext(), R.color.oxblood))
                }
                else {
                    viewModel.deleteFavourite(passedPost)
                    binding.likePostIv.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
                }
            }
        })

        }

        connectivityLiveData.observe(viewLifecycleOwner,{ networkState->
            if (networkState == true){
                 passedPost.id?.let { viewModel.getComments(it) }
            }
        })


        //Setting up the button to send the comment
        binding.sendComment.setOnClickListener {
            val sentComment=comment.text.toString()
            val completeComment = Comments(sentComment,"Example@gmail.com",12,"Name Surname", 2)
                viewModel.sendComment(completeComment)
                comment.text=null
        }

        // the intent that shares a post
        binding.sharePost.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Title: "+passedPost.title)
                putExtra(Intent.EXTRA_TEXT, "Body: "+passedPost.body)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        setupRecyclerView()
        //Observe the comment list live data in the view model
        viewModel.commentList.observe(viewLifecycleOwner, { response->
            when(response){
                is Resource.Success->{
                    response.data?.let{comments ->
                        comments.forEach {
                             compiledComments.add(it)
                        }
                        //Toggle progress bar visibility
                        binding.commentProgressbar.visibility= View.INVISIBLE
                        //Pass comment list to adapter
                        commentsRecyclerAdapter.differ.submitList(compiledComments)
                    }
                }
                is Resource.Error->{
                    response.message?.let { message->
                        binding.commentProgressbar.visibility= View.INVISIBLE
                        Toast.makeText(activity,"Error: $message",Toast.LENGTH_LONG).show()
                        passedPost.id?.let { viewModel.getComments(it) }
                    }
                }
                is Resource.Loading -> { binding.commentProgressbar.visibility= View.VISIBLE }
            }
        })

        //Observe the sent comment live data in the view model
        viewModel.sentComment.observe(viewLifecycleOwner, { response->
            when(response){
                is Resource.Success->{
                    response.data?.let{comment ->
                        binding.commentProgressbar.visibility= View.INVISIBLE
                        compiledComments.add(comment)
                        commentsRecyclerAdapter.differ.submitList(compiledComments)
                    }
                }
                is Resource.Error->{
                    response.message?.let { message->
                        binding.commentProgressbar.visibility= View.INVISIBLE
                        Toast.makeText(activity,message,Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> { binding.commentProgressbar.visibility= View.VISIBLE }
            }
        })
    }

override fun onDestroy(){
    super.onDestroy()
    //Set the comment live data to null when the fragment is destroyed
    viewModel.commentList.value=null
}

private fun setupRecyclerView(){
    commentsRecyclerAdapter = CommentsRecyclerAdapter()
    binding.postCommentsRv.apply {
        adapter = commentsRecyclerAdapter
        layoutManager = LinearLayoutManager(activity)
    }
}
}