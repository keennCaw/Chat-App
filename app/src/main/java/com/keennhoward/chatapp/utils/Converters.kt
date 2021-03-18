package com.keennhoward.chatapp.utils

import android.text.format.DateFormat
import java.util.*

object Converters {
    fun getDate(timestamp: Long) :String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp * 1000L
        val date = DateFormat.format("dd-MM-yyyy",calendar).toString()
        return date
    }
}