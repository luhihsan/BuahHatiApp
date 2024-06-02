package com.modul.buahhati.data.remote

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Session")
class LoginPreference private constructor(private val dataStore: DataStore<Preferences>){

    suspend fun login(){
        dataStore.edit { preferences ->
            preferences[LOGIN_KEY] = true
        }
    }

    fun getLoginStatus(): Flow<Boolean?> {
        return dataStore.data.map { preferences ->
            preferences[LOGIN_KEY]
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = ""
            preferences[LOGIN_KEY] = false
        }
    }

    fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    suspend fun svToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginPreference? = null

        private val TOKEN_KEY = stringPreferencesKey("token")
        private val LOGIN_KEY = booleanPreferencesKey("is_login")

        fun getInstance(dataStore: DataStore<Preferences>): LoginPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = LoginPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}