package com.example.knr_japota

import com.example.knr_japota.api.AuthApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SupabaseConfig {

    // Ganti dengan Project URL
    private const val SUPABASE_URL =
        "https://pzhftxtbzlvpmceqrfrq.supabase.co/"

    // Ganti dengan Publishable API Key
    private const val SUPABASE_KEY =
        "sb_publishable_mUMTweG5XPcmeOI-uzJs-w_HA83qvTQ"

    fun createRetrofit(accessToken: String? = null): Retrofit {

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->

                val request = chain.request()
                    .newBuilder()
                    .addHeader("apikey", SUPABASE_KEY)
                    .addHeader("Accept-Profile", "mobile")
                    .addHeader("Content-Profile", "mobile")

                // Token login pengguna akan ditambahkan nanti
                if (!accessToken.isNullOrBlank()) {
                    request.addHeader(
                        "Authorization",
                        "Bearer $accessToken"
                    )
                }

                chain.proceed(request.build())
            }
            .build()

        return Retrofit.Builder()
            .baseUrl("${SUPABASE_URL}rest/v1/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun createAuthApi(): AuthApi {
        return Retrofit.Builder()
            .baseUrl(SUPABASE_URL + "auth/v1/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request()
                            .newBuilder()
                            .addHeader("apikey", SUPABASE_KEY)
                            .build()

                        chain.proceed(request)
                    }
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }
}
