package com.glebalekseevjk.floatify_android.domain.interactor

import com.glebalekseevjk.floatify_android.domain.entity.Resource
import com.glebalekseevjk.floatify_android.domain.entity.translate.TranslateLang
import com.glebalekseevjk.floatify_android.domain.exception.ExpiredSessionException
import com.glebalekseevjk.floatify_android.domain.repository.TemporaryRepository
import com.glebalekseevjk.floatify_android.domain.repository.TranslateRepository
import com.glebalekseevjk.floatify_android.domain.repository.YandexSessionRepository
import javax.inject.Inject

class TranslateUseCase @Inject constructor(
    private val translateRepository: TranslateRepository,
    private val temporaryRepository: TemporaryRepository,
    private val yandexSessionRepository: YandexSessionRepository
) {
    suspend fun translateText(
        textList: List<String>,
        sourceLang: TranslateLang,
        targetLang: TranslateLang
    ): Resource<List<String>> {
        val result = getSessionAndTranslateText(textList, sourceLang, targetLang)
        return if (result is Resource.Failure && result.throwable is ExpiredSessionException) {
            temporaryRepository.setYandexTranslateSessionId(null)
            getSessionAndTranslateText(textList, sourceLang, targetLang)
        } else result
    }

    private suspend fun getSessionAndTranslateText(
        textList: List<String>,
        sourceLang: TranslateLang,
        targetLang: TranslateLang
    ): Resource<List<String>> {
        val sessionId = temporaryRepository.getYandexTranslateSessionId()
            ?: yandexSessionRepository.refreshYandexSessionId().also {
                if (it == null) return Resource.Failure(
                    RuntimeException()
                ) else temporaryRepository.setYandexTranslateSessionId(it)
            }!!
        return translateRepository.translateText(textList, sourceLang, targetLang, sessionId)
    }
}