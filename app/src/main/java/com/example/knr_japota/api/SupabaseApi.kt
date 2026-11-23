package com.example.knr_japota.api

import com.example.knr_japota.Model.Booking
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import com.example.knr_japota.SupabaseConfig.createRetrofit
import com.example.knr_japota.Model.Perlengkapan
import com.example.knr_japota.Model.UserProfile
import com.example.knr_japota.Model.BookingUpdateRequest
import retrofit2.http.PATCH
import retrofit2.http.DELETE

interface SupabaseApi {
    @GET("bookings")
    suspend fun getBookings(
        @Query("select") select: String = "*",
        @Query("order") order: String = "created_at.desc"
    ): Response<List<Booking>>

    @GET("sewa_perlengkapan")
    suspend fun getPerlengkapan(
        @Query("select") select: String = "*",
        @Query("order") order: String = "created_at.desc"
    ): Response<List<Perlengkapan>>

    @Headers("Prefer: return=representation")
    @POST("sewa_perlengkapan")
    suspend fun createPerlengkapan(
        @Body perlengkapan: Perlengkapan
    ): Response<List<Perlengkapan>>

    @Headers("Prefer: return=representation")
    @POST("bookings")
    suspend fun createBooking(
        @Body booking: Booking
    ): Response<List<Booking>>

    @GET("users")
    suspend fun getUserProfile(
        @Query("id") idFilter: String,
        @Query("select") select: String = "*"
    ): Response<List<UserProfile>>

    @Headers("Prefer: return=representation")
    @POST("users")
    suspend fun createUserProfile(
        @Body profile: UserProfile
    ): Response<List<UserProfile>>

    @Headers("Prefer: return=representation")
    @PATCH("bookings")
    suspend fun updateBooking(
        @Query("id") idFilter: String,
        @Query("user_id") userIdFilter: String,
        @Body request: BookingUpdateRequest
    ): Response<List<Booking>>

    @DELETE("bookings")
    suspend fun deleteBooking(
        @Query("id") idFilter: String,
        @Query("user_id") userIdFilter: String
    ): Response<Unit>

    fun createApi(accessToken: String? = null): SupabaseApi {
        return createRetrofit(accessToken)
            .create(SupabaseApi::class.java)
    }
}
