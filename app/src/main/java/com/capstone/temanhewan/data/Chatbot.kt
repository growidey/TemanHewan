package com.capstone.temanhewan.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chatbot(
    val message: String,
    val id: String,
    val time: String
): Parcelable
