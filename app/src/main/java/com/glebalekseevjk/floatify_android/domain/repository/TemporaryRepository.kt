package com.glebalekseevjk.floatify_android.domain.repository

interface TemporaryRepository {
    fun getYandexTranslateSessionId(): String?
    fun setYandexTranslateSessionId(sessionId: String?)
    fun clear()
}