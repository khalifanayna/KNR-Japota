package com.example.knr_japota.Model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("access_token")
    val accessToken: String? = null,

    @SerializedName("refresh_token")
    val refreshToken: String? = null,

    @SerializedName("token_type")
    val tokenType: String? = null,

    val user: AuthUser? = null
)

data class AuthUser(
    val id: String,
    val email: String?
)