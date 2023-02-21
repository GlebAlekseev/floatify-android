package com.glebalekseevjk.floatify_android.domain.repository

interface TemporaryRepository {
    suspend fun getYandexTranslateSessionId(): String?
    suspend fun setYandexTranslateSessionId(sessionId: String)
    suspend fun clear()
}