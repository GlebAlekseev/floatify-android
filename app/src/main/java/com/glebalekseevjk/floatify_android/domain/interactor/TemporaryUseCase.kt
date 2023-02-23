package com.glebalekseevjk.floatify_android.domain.interactor

import com.glebalekseevjk.floatify_android.domain.repository.TemporaryRepository
import javax.inject.Inject

class TemporaryUseCase @Inject constructor(
    private val temporaryRepository: TemporaryRepository
) {
    fun getYandexTranslateSessionId() = temporaryRepository.getYandexTranslateSessionId()

    fun setYandexTranslateSessionId(sessionId: String?) =
        temporaryRepository.setYandexTranslateSessionId(sessionId)

    fun clear() = temporaryRepository.clear()
}