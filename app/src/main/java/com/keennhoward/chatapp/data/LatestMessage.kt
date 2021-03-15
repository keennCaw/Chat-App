package com.keennhoward.chatapp.data

data class LatestMessage(
    val id:String,
    val text:String,
    val fromId:String,
    val toId:String,
    val timeStamp:Long,
    val username:String,
    val profileImageUrl:String,
    val read:Boolean
) {
    constructor():this("","","","",-1,"","", false)
}