package com.glebalekseevjk.floatify_android.domain.repository

interface YandexSessionRepository {
    suspend fun refreshYandexSessionId(): String?
    fun yandexSessionWebViewActivityStopped(isGood: Boolean)
}