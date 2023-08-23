package com.glebalekseevjk.floatify_android.data.free_translate

import com.glebalekseevjk.floatify_android.data.free_translate.dto.FreeTranslationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FreeTranslateService {
    @GET("translate")
    suspend fun translateText(
        @Query("dl") destinationLanguage: String,
        @Query("text") text: String
    ): Response<FreeTranslationResponse>
}