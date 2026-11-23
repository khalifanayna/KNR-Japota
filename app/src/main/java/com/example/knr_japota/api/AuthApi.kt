package com.example.knr_japota.api

import com.example.knr_japota.Model.AuthRequest
import com.example.knr_japota.Model.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @POST("signup")
    suspend fun register(
        @Body request: AuthRequest
    ): Response<AuthResponse>

    @POST("token")
    suspend fun login(
        @Query("grant_type") grantType: String = "password",
        @Body request: AuthRequest
    ): Response<AuthResponse>
}