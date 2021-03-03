package com.keennhoward.chatapp

data class ChatMessage(
    val id:String,
    val text:String,
    val fromId:String,
    val toId:String,
    val timeStamp:Long,
    val read:Boolean
    ){
    constructor(): this("","","","",-1, false)
}