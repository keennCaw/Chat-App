package com.keennhoward.chatapp.data

data class GlobalMessage(
    val fromId:String,
    val username:String,
    val text:String,
    val profileImageUrl:String,
    val timeStamp:Long
)