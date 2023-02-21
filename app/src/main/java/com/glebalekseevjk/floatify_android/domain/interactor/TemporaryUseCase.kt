package com.glebalekseevjk.floatify_android.domain.interactor

import com.glebalekseevjk.floatify_android.domain.repository.TemporaryRepository
import javax.inject.Inject

class TemporaryUseCase @Inject constructor(
    private val temporaryRepository: TemporaryRepository
) {
    suspend fun getYandexTranslateSessionId() = temporaryRepository.getYandexTranslateSessionId()
    suspend fun setYandexTranslateSessionId(sessionId: String) = temporaryRepository.setYandexTranslateSessionId(sessionId)
    suspend fun clear() = temporaryRepository.clear()
}