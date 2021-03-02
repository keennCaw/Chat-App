package com.keennhoward

import androidx.recyclerview.widget.RecyclerView
import com.keennhoward.chatapp.ChatMessage
import com.keennhoward.chatapp.databinding.LatestMessagesItemBinding

class MessagesAdapter() {
}

class MessagesViewHolder(
    private val binding: LatestMessagesItemBinding
): RecyclerView.ViewHolder(binding.root){
    fun bind(chatMessage: ChatMessage){
        binding.latestMessage.text = chatMessage.text
    }
}