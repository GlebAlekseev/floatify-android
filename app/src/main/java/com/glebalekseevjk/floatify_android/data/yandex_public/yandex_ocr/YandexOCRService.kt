package com.glebalekseevjk.floatify_android.data.yandex_public.yandex_ocr

import com.glebalekseevjk.floatify_android.data.yandex_public.yandex_ocr.dto.YandexOCRResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface YandexOCRService {

    @Multipart
    @POST("ocr/v1.1/recognize")
    suspend fun recognizeText(
        @QueryMap options: Map<String, String>,
        @Part file: MultipartBody.Part
    ): Response<YandexOCRResponse>

    companion object {
        val baseOptions = mapOf(
            "srv" to "tr-image",
            "lang" to "*",
            "rotate" to "auto",
        )
    }
}