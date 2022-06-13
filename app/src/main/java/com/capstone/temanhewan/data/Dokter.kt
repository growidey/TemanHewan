package com.capstone.temanhewan.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dokter(
    var name: String,
    var price: String,
    var avatar: Int
): Parcelable