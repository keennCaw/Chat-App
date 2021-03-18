package com.keennhoward.chatapp.data

data class LatestMessage(
    val id:String,
    val text:String,
    val fromId:String,
    val toId:String,
    val timeStamp:Long,
    val username:String,
    val profileImageUrl:String,
    val read:Boolean,
    val token:String,
    val email:String
) {
    constructor():this("","","","",-1,"","", false, "","")
}