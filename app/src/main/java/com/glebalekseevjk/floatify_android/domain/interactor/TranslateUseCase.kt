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
    // TODO stack overflow if always http 403 - fix
    suspend fun translateText(
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
        val result = translateRepository.translateText(textList, sourceLang, targetLang, sessionId)
        return when (result) {
            is Resource.Failure -> {
                when (result.throwable) {
                    is ExpiredSessionException -> {
                        temporaryRepository.setYandexTranslateSessionId(null)
                        translateText(textList, sourceLang, targetLang)
                    }
                    else -> result
                }
            }
            is Resource.Success -> result
        }
    }
}