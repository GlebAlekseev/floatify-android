package com.glebalekseevjk.floatify_android.domain.interactor

import com.glebalekseevjk.floatify_android.domain.repository.YandexSessionRepository
import javax.inject.Inject

class YandexSessionUseCase @Inject constructor(
    private val yandexSessionRepository: YandexSessionRepository
) {
    suspend fun refreshYandexSessionId() = yandexSessionRepository.refreshYandexSessionId()

    fun yandexSessionWebViewActivityStopped(isGood: Boolean) =
        yandexSessionRepository.yandexSessionWebViewActivityStopped(isGood)
}