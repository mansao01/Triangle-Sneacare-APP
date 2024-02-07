package com.mansao.trianglesneacare.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("triangle")

class AppPreferences @Inject constructor(@ApplicationContext val context: Context) {
    private val dataStore = context.dataStore

    private val accessTokenKey = stringPreferencesKey("access_token")
    private val refreshTokenKey = stringPreferencesKey("refresh_token")
    private val isLoginState = booleanPreferencesKey("is_login")
    private val username = stringPreferencesKey("username")
    private val roleName = stringPreferencesKey("role")
    private val showBalloonState = booleanPreferencesKey("show_balloon")

    //    save
    suspend fun saveAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[accessTokenKey] = token
        }
    }

    suspend fun saveRefreshToken(token: String) {
        dataStore.edit { preferences ->
            preferences[refreshTokenKey] = token
        }
    }

    suspend fun saveIsLoginState(isLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[isLoginState] = isLogin
        }
    }

    suspend fun saveUsername(name: String) {
        dataStore.edit { preferences ->
            preferences[username] = name
        }
    }

    suspend fun saveRole(role: String) {
        dataStore.edit { preferences ->
            preferences[roleName] = role
        }
    }

    suspend fun saveShowBalloonState(showBalloon: Boolean) {
        dataStore.edit { preferences ->
            preferences[showBalloonState] = showBalloon
        }
    }

    //    get
    suspend fun getAccessToken(): String? {
        val preferences = dataStore.data.first()
        return preferences[accessTokenKey]
    }

    suspend fun getRefreshToken(): String? {
        val preferences = dataStore.data.first()
        return preferences[refreshTokenKey]
    }

    suspend fun getUsername(): String? {
        val preferences = dataStore.data.first()
        return preferences[username]
    }

    suspend fun getRole(): String? {
        val preferences = dataStore.data.first()
        return preferences[roleName]
    }

    fun getIsLoginState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val loginState = preferences[isLoginState] ?: false
                loginState
            }
    }

    fun getShowBalloonState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val balloonState = preferences[showBalloonState] ?: true
                balloonState
            }
    }

//    remove

    suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(accessTokenKey)
            preferences.remove(refreshTokenKey)
        }
    }

    suspend fun clearUsername() {
        dataStore.edit { preferences ->
            preferences.remove(username)
        }
    }

    suspend fun clearRoleName() {
        dataStore.edit { preferences ->
            preferences.remove(roleName)
        }
    }
}