package com.glebalekseevjk.floatify_android.data.yandex_translate

import com.glebalekseevjk.floatify_android.data.yandex_translate.dto.YandexTranslateResponse
import com.glebalekseevjk.floatify_android.data.yandex_translate.exception.ExpiredSessionException
import com.glebalekseevjk.floatify_android.domain.entity.Resource
import com.glebalekseevjk.floatify_android.domain.entity.translate.TranslateLang
import com.glebalekseevjk.floatify_android.domain.interactor.TemporaryUseCase
import com.glebalekseevjk.floatify_android.domain.interactor.YandexSessionUseCase
import com.glebalekseevjk.floatify_android.domain.repository.TranslateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class YandexTranslateRepositoryImpl @Inject constructor(
    private val yandexTranslateService: YandexTranslateService,
    private val temporaryUseCase: TemporaryUseCase,
    private val yandexSessionUseCase: YandexSessionUseCase
) : TranslateRepository {
    override suspend fun translateText(
        textList: List<String>,
        sourceLang: TranslateLang,
        targetLang: TranslateLang
    ): Resource<List<String>> = withContext(Dispatchers.IO) {
        if (textList.isEmpty()) return@withContext Resource.Success(emptyList<String>())
        val result = getSessionAndTranslateText(textList, sourceLang, targetLang)
        return@withContext if (result is Resource.Failure && result.throwable is ExpiredSessionException) {
            temporaryUseCase.setYandexTranslateSessionId(null)
            getSessionAndTranslateText(textList, sourceLang, targetLang)
        } else result
    }

    private suspend fun getSessionAndTranslateText(
        textList: List<String>,
        sourceLang: TranslateLang,
        targetLang: TranslateLang
    ): Resource<List<String>> {
        val sessionId = temporaryUseCase.getYandexTranslateSessionId()
            ?: yandexSessionUseCase.refreshYandexSessionId().also {
                if (it == null) return Resource.Failure(
                    RuntimeException()
                ) else temporaryUseCase.setYandexTranslateSessionId(it)
            }!!

        val options = YandexTranslateService.baseOptions.toMutableMap()
        options["source_lang"] = when (sourceLang) {
            TranslateLang.EN -> "en"
            TranslateLang.RU -> "ru"
        }
        options["target_lang"] = when (targetLang) {
            TranslateLang.EN -> "en"
            TranslateLang.RU -> "ru"
        }
        options["id"] = sessionId
        val response = yandexTranslateService.translateText(options, *textList.toTypedArray())
        return getResourceFromYandexTranslateResponse(response)
    }

    private fun getResourceFromYandexTranslateResponse(response: Response<YandexTranslateResponse>): Resource<List<String>> {
        response.code().let {
            when (it) {
                200 -> {}
                403 -> return Resource.Failure<Nothing>(ExpiredSessionException())
                else -> return Resource.Failure<Nothing>(Exception())
            }
        }
        val body = response.body()!!
        return Resource.Success(body.text)
    }
}