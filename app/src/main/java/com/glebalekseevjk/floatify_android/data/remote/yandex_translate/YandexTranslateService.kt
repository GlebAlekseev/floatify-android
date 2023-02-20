package com.glebalekseevjk.floatify_android.data.remote.yandex_translate

import com.glebalekseevjk.floatify_android.data.remote.yandex_translate.dto.YandexTranslateResponse
import retrofit2.Response
import retrofit2.http.*

interface YandexTranslateService {
    @FormUrlEncoded
    @POST("api/v1/tr.json/translate")
    suspend fun translateText(
        @QueryMap options: Map<String, String>,
        @Field("text") vararg text: String,
    ): Response<YandexTranslateResponse>

    companion object {
        val baseOptions = mapOf(
            "srv" to "tr-image",
            "source_lang" to "ru",
            "target_lang" to "en",
            "format" to "html",
        )
    }
}