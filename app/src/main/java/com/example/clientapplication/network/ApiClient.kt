package com.example.clientapplication.network


import android.content.Context
import com.example.clientapplication.localStorage.Storage
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class ApiClient(private val context: Context) {
    private val BASE_URL = "https://5b5c-92-184-100-216.ngrok.io"


    private val retrofit: Retrofit by lazy {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val httpClient = OkHttpClient.Builder()

        // Récupération du token à partir du contexte
        val token = retrieveTokenFromStorage(context)
        if (token.isNotEmpty() && token != "") {
            val authInterceptor = AuthInterceptor(token)
            httpClient.addInterceptor(authInterceptor)
        }

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(httpClient.build())
            .build()
    }

    private fun retrieveTokenFromStorage(context: Context): String {
        return Storage(context).getToken()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}