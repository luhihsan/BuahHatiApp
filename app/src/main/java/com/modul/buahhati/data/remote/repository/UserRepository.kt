package com.modul.buahhati.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.modul.buahhati.data.remote.LoginPreference
import com.modul.buahhati.data.remote.Result
import com.modul.buahhati.data.remote.response.ErrorResponse
import com.modul.buahhati.data.remote.retrofit.ApiService
import retrofit2.HttpException

class UserRepository (
    private var apiService: ApiService,
    private var loginPreference: LoginPreference
){

    fun register(username: String, name: String, email: String, password: String): LiveData<Result<ErrorResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.register(name, email, password, username)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    val jsonElement = JsonParser().parse(jsonInString)
                    if (jsonElement.isJsonObject) {
                        val error = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                        error.message.toString()
                    } else {
                        jsonInString
                    }
                } catch (jsonEx: JsonSyntaxException) {
                    jsonInString
                } catch (illegalStateEx: IllegalStateException) {
                    jsonInString
                }
                emit(Result.Error(errorMessage))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(apiService: ApiService, preferences: LoginPreference): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, preferences).also { instance = it }
            }
    }
}