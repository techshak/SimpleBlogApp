package com.olamachia.simpleblogappwithdatabinding.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.olamachia.simpleblogappwithdatabinding.R
import com.olamachia.simpleblogappwithdatabinding.models.dataclasses.Comments

class CommentsRecyclerAdapter: RecyclerView.Adapter<CommentsRecyclerAdapter.CommentsViewHolder>() {

    private lateinit var name: TextView
    private lateinit var email : TextView
    private lateinit var body : TextView

    inner class CommentsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        init {
            name= itemView.findViewById(R.id.comment_name_tv)
            email= itemView.findViewById(R.id.comment_mail_tv)
            body= itemView.findViewById(R.id.comment_body_tv)
        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<Comments>(){
        override fun areItemsTheSame(oldItem: Comments, newItem: Comments): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comments, newItem: Comments): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return CommentsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_comment_item,parent,false))
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val comment = differ.currentList[position]

        holder.itemView.apply {
            name.text = comment.name
            email.text= comment.email
            body.text= comment.body
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}