package com.capstone.temanhewan.data

data class User(
    val name: String,
    val id: String,
    val email: String,
    val phoneNumber: String?,
){
    constructor():this("","","","")
}