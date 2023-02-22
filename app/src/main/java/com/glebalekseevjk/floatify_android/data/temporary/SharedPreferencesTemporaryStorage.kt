package com.glebalekseevjk.floatify_android.data.temporary

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferencesTemporaryStorage @Inject constructor(private val context: Context) {
    private val temporaryPref: SharedPreferences =
        context.getSharedPreferences(PREF_PACKAGE_NAME, Context.MODE_PRIVATE)

    fun getYandexTranslateSessionId(): String? {
        return temporaryPref.getString(PREF_KEY_YANDEX_TRANSLATE_SESSION_ID, null)
    }

    fun setYandexTranslateSessionId(sessionId: String?) {
        temporaryPref.edit().putString(PREF_KEY_YANDEX_TRANSLATE_SESSION_ID, sessionId).apply()
    }

    fun clear() {
        temporaryPref.edit().remove(PREF_KEY_YANDEX_TRANSLATE_SESSION_ID).apply()
    }

    companion object {
        private const val PREF_PACKAGE_NAME = "com.glebalekseevjk.floatify"
        private const val PREF_KEY_YANDEX_TRANSLATE_SESSION_ID = "yandex_translate_session_id"
    }
}