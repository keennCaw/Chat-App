package com.keennhoward.chatapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val uid:String,
    val username:String,
    val profileImageUrl:String,
    val email:String,
    val token:String?
) : Parcelable {
    constructor():this("","","","","")
}