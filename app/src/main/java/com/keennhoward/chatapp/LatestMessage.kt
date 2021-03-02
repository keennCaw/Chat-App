package com.keennhoward.chatapp

data class LatestMessage(
    val id:String,
    val text:String,
    val fromId:String,
    val toId:String,
    val timeStamp:Long,
    val uid:String,
    val username:String,
    val profileImageUrl:String
) {
    constructor():this("","","","",-1,"","","")
}