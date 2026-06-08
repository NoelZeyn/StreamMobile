package com.example.stream.Data.Local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")
object UserPreferences {
    private val TOKEN_KEY = stringPreferencesKey("auth_token")
    private val USER_ID_KEY = intPreferencesKey("user_id")
    private val NAME_KEY = stringPreferencesKey("name")
    private val EMAIL_KEY = stringPreferencesKey("email")
    private val CHANNEL_NAME = stringPreferencesKey("channel_name")

    suspend fun saveUserData(context: Context, token: String, userId: Int, nama: String, email: String, channel_name: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[USER_ID_KEY] = userId
            prefs[NAME_KEY] = nama
            prefs[EMAIL_KEY] = email
            prefs[CHANNEL_NAME] = channel_name
        }
    }

    fun getToken(context: Context): Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }

    fun getUserId(context: Context): Flow<Int?> = context.dataStore.data.map { it[USER_ID_KEY] }
    fun getNama(context: Context): Flow<String?> = context.dataStore.data.map { it[NAME_KEY] }
    fun getEmail(context: Context): Flow<String?> = context.dataStore.data.map { it[EMAIL_KEY] }
    fun getNoTelp(context: Context): Flow<String?> = context.dataStore.data.map { it[CHANNEL_NAME] }


    suspend fun clearUserData(context: Context) {
        context.dataStore.edit {
            it.remove(TOKEN_KEY)
            it.remove(USER_ID_KEY)
            it.remove(NAME_KEY)
            it.remove(EMAIL_KEY)
            it.remove(CHANNEL_NAME)
        }
    }
}