package com.keennhoward.chatapp.views.globalchat

import android.app.Application
import android.view.View
import com.bumptech.glide.Glide
import com.keennhoward.chatapp.R
import com.keennhoward.chatapp.databinding.ChatLogToBinding
import com.keennhoward.chatapp.databinding.GlobalChatToBinding

import com.xwray.groupie.viewbinding.BindableItem


class GlobalChatToItem(
    private val text: String,
    private val imageUrl: String,
    private val application: Application,
    private val username:String
): BindableItem<GlobalChatToBinding>(){
    override fun getLayout(): Int {
        return R.layout.global_chat_to
    }

    override fun bind(viewBinding: GlobalChatToBinding, position: Int) {
        viewBinding.chatItemMessage.text = text
        Glide.with(application)
            .load(imageUrl)
            .into(viewBinding.chatItemImageView)
        viewBinding.chatItemUsername.text = username
    }

    override fun initializeViewBinding(view: View): GlobalChatToBinding {
        return GlobalChatToBinding.bind(view)
    }

}