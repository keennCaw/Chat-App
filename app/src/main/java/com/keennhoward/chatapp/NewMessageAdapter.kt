package com.keennhoward.chatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.keennhoward.chatapp.databinding.NewMessageUserItemBinding

class NewMessageAdapter(private val context: Context, private val listener: UserClickListener):
ListAdapter<User, UserViewHolder>(UserDiffUtil()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = NewMessageUserItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserViewHolder(binding,listener,context)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getUserAt(position: Int):User{
        return getItem(position)
    }
}

interface UserClickListener{
    fun onUserItemClickListener(user:User)
}

class UserViewHolder(
    private val binding: NewMessageUserItemBinding,
    private val listener: UserClickListener,
    private val context: Context
):RecyclerView.ViewHolder(binding.root){

    fun bind(user:User){

        Glide.with(context)
            .load(user.profileImageUrl)
            .into(binding.userProfileImage)

        binding.userUsername.text = user.username

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