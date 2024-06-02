package com.modul.buahhati.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.modul.buahhati.data.remote.LoginPreference
import com.modul.buahhati.data.remote.repository.UserRepository
import com.modul.buahhati.di.Injection
import com.modul.buahhati.view.sign_up.SignUpViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val prereference : LoginPreference
):ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECK_CAST")
    override fun <T:ViewModel>create(modelClass:Class<T>):T{
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class" + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context, preferences: LoginPreference): ViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(Injection.provideRepository(context), preferences)
            }.also { INSTANCE = it }
    }
}