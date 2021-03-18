package com.keennhoward.chatapp.views.main.messages

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.keennhoward.chatapp.data.LatestMessage
import com.keennhoward.chatapp.R
import com.keennhoward.chatapp.databinding.LatestMessagesItemBinding

class MessagesAdapter(
    private val listener: MessageItemClickListener,
    private val context: Context
) :
    ListAdapter<LatestMessage, MessagesViewHolder>(
        CartDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val latestMessageBinding =
            LatestMessagesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MessagesViewHolder(
            latestMessageBinding,
            context,
            listener
        )
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

interface MessageItemClickListener {
    fun onMessageItemClickListener(messageInfo: LatestMessage)
}

class MessagesViewHolder(
    private val binding: LatestMessagesItemBinding,
    private val context: Context,
    private val listener: MessageItemClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(messageInfo: LatestMessage) {
        if(!messageInfo.read){
            binding.latestMessage.typeface = Typeface.DEFAULT_BOLD
            binding.latestMessage.setTextColor(ContextCompat.getColor(context, R.color.black))
            binding.latestMessageUsername.typeface = Typeface.DEFAULT_BOLD
            binding.latestMessageUsername.setTextColor(ContextCompat.getColor(context, R.color.black))
        }
        binding.latestMessage.text = messageInfo.text
        binding.latestMessageUsername.text = messageInfo.username
        Glide.with(context)
            .load(messageInfo.profileImageUrl)
            .into(binding.latestMessageImage)

        binding.root.setOnClickListener {
            listener.onMessageItemClickListener(messageInfo)
        }
    }
}

class CartDiffUtil : DiffUtil.ItemCallback<LatestMessage>() {
    override fun areItemsTheSame(oldItem: LatestMessage, newItem: LatestMessage): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LatestMessage, newItem: LatestMessage): Boolean {
        return oldItem == newItem
    }
}