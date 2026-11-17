package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.rcrDataStore: DataStore<Preferences> by preferencesDataStore(name = "rcr_settings")

data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.LIGHT,
    val languageCode: String = "fr",
    val isRtlLayout: Boolean = false,
    val inAppAlertsEnabled: Boolean = true,
    val isAdminAuthenticated: Boolean = false,
    val adminPin: String = DEFAULT_ADMIN_PIN,
    val clubLogoPath: String = ""
) {
    companion object {
        const val DEFAULT_ADMIN_PIN = "1934"
    }
}

class PreferencesManager(private val context: Context) {

    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val LANGUAGE_CODE = stringPreferencesKey("language_code")
        val IS_RTL_LAYOUT = booleanPreferencesKey("is_rtl_layout")
        val IN_APP_ALERTS = booleanPreferencesKey("in_app_alerts")
        val IS_ADMIN_AUTH = booleanPreferencesKey("is_admin_authenticated")
        val ADMIN_PIN = stringPreferencesKey("admin_pin")
        val CLUB_LOGO_PATH = stringPreferencesKey("club_logo_path")
    }

    val preferencesFlow: Flow<UserPreferences> = context.rcrDataStore.data.map { prefs ->
        val themeStr = prefs[Keys.THEME_MODE] ?: ThemeMode.LIGHT.name
        val theme = runCatching { ThemeMode.valueOf(themeStr) }.getOrDefault(ThemeMode.LIGHT)
        val lang = prefs[Keys.LANGUAGE_CODE] ?: "fr"
        val isRtl = prefs[Keys.IS_RTL_LAYOUT] ?: (lang == "ar")
        UserPreferences(
            themeMode = theme,
            languageCode = lang,
            isRtlLayout = isRtl,
            inAppAlertsEnabled = prefs[Keys.IN_APP_ALERTS] ?: true,
            isAdminAuthenticated = prefs[Keys.IS_ADMIN_AUTH] ?: false,
            adminPin = prefs[Keys.ADMIN_PIN] ?: UserPreferences.DEFAULT_ADMIN_PIN,
            clubLogoPath = prefs[Keys.CLUB_LOGO_PATH].orEmpty()
        )
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.rcrDataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    suspend fun setLanguageAndRtl(languageCode: String, isRtl: Boolean) {
        context.rcrDataStore.edit {
            it[Keys.LANGUAGE_CODE] = languageCode
            it[Keys.IS_RTL_LAYOUT] = isRtl
        }
    }

    suspend fun setInAppAlertsEnabled(enabled: Boolean) {
        context.rcrDataStore.edit { it[Keys.IN_APP_ALERTS] = enabled }
    }

    suspend fun setAdminAuthenticated(authenticated: Boolean) {
        context.rcrDataStore.edit { it[Keys.IS_ADMIN_AUTH] = authenticated }
    }

    suspend fun updateAdminPin(newPin: String) {
        context.rcrDataStore.edit { it[Keys.ADMIN_PIN] = newPin }
    }

    suspend fun setClubLogoPath(path: String) {
        context.rcrDataStore.edit { it[Keys.CLUB_LOGO_PATH] = path }
    }
}
