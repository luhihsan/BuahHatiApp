package com.modul.buahhati.data.remote.retrofit

import com.modul.buahhati.data.remote.response.ErrorResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("Register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") pass: String,
    ): ErrorResponse
}