package com.glebalekseevjk.floatify_android.data.repository

import com.glebalekseevjk.floatify_android.data.remote.yandex_translate.YandexTranslateService
import com.glebalekseevjk.floatify_android.data.remote.yandex_translate.dto.YandexTranslateResponse
import com.glebalekseevjk.floatify_android.domain.entity.Resource
import com.glebalekseevjk.floatify_android.domain.entity.translate.TranslateLang
import com.glebalekseevjk.floatify_android.domain.repository.TranslateRepository
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class YandexTranslateRepositoryImpl @Inject constructor(
    private val yandexTranslateService: YandexTranslateService
) : TranslateRepository {
    override suspend fun translateText(
        textList: List<String>,
        sourceLang: TranslateLang,
        targetLang: TranslateLang
    ): Resource<List<String>> = with(Dispatchers.IO) {
        val options = YandexTranslateService.baseOptions.toMutableMap()
        options["source_lang"] = when (sourceLang) {
            TranslateLang.EN -> "en"
            TranslateLang.RU -> "ru"
        }
        options["target_lang"] = when (targetLang) {
            TranslateLang.EN -> "en"
            TranslateLang.RU -> "ru"
        }
        val response = yandexTranslateService.translateText(options, *textList.toTypedArray())
        return@with getResourceFromYandexTranslateResponse(response)
    }

    private fun getResourceFromYandexTranslateResponse(response: Response<YandexTranslateResponse>): Resource<List<String>> {
        response.code().let {
            when (it) {
                200 -> {}
                else -> return Resource.Failure<Nothing>(HttpException(response))
            }
        }
        val body = response.body()
            ?: return Resource.Failure<Nothing>(NullPointerException("response.body is null"))
        return Resource.Success(body.text)
    }
}