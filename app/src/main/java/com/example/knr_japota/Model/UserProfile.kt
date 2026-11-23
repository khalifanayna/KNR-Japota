package com.example.knr_japota.Model

data class UserProfile(
    val id: String,
    val nama: String,
    val email: String,
    val username: String,
    val role: String = "user"
)