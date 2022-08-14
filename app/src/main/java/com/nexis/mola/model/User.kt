package com.nexis.mola.model

data class User(
    val userId: String = "",
    val userEmail: String = "",
    val userFullName: String = "",
    val userNick: String = "",
    val userGender: String = "",
    val userAge: Int = 0,
    val userProfileUrl: String = "",
    val userSeconds: Int = 0
)
