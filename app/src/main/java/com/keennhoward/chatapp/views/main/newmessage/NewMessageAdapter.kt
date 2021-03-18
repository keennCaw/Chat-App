package com.keennhoward.chatapp.views.main.newmessage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.keennhoward.chatapp.data.User
import com.keennhoward.chatapp.databinding.NewMessageUserItemBinding
import java.util.*
import kotlin.collections.ArrayList

class NewMessageAdapter(private val context: Context, private val listener: UserClickListener):
ListAdapter<User, UserViewHolder>(
    UserDiffUtil()
){

    private var unfilteredList = ArrayList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = NewMessageUserItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserViewHolder(
            binding,
            listener,
            context
        )
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getUserAt(position: Int): User {
        return getItem(position)
    }

    fun modifyList(list : ArrayList<User>) {
        unfilteredList = list
        submitList(list)
    }

    fun filter(query: CharSequence?) {
        val list = mutableListOf<User>()

        // perform the data filtering
        if(!query.isNullOrEmpty()) {
            list.addAll(unfilteredList.filter {
                it.username.toLowerCase(Locale.getDefault()).contains(query.toString().toLowerCase(Locale.getDefault())) ||
                        it.email.toLowerCase(Locale.getDefault()).contains(query.toString().toLowerCase(Locale.getDefault())) })
        } else {
            list.addAll(unfilteredList)
        }

        submitList(list)
    }

}

interface UserClickListener{
    fun onUserItemClickListener(user: User)
}

class UserViewHolder(
    private val binding: NewMessageUserItemBinding,
    private val listener: UserClickListener,
    private val context: Context
):RecyclerView.ViewHolder(binding.root){

    fun bind(user: User){

        Glide.with(context)
            .load(user.profileImageUrl)
            .into(binding.userProfileImage)

        binding.userUsername.text = user.username
        binding.userEmail.text = user.email

        binding.root.setOnClickListener {
            listener.onUserItemClickListener(user)
        }
    }

}

class UserDiffUtil: DiffUtil.ItemCallback<User>(){
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}