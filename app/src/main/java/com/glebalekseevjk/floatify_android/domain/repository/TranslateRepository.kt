package com.glebalekseevjk.floatify_android.domain.repository

import com.glebalekseevjk.floatify_android.domain.entity.Resource
import com.glebalekseevjk.floatify_android.domain.entity.translate.TranslateLang


interface TranslateRepository {
    suspend fun translateText(
        textList: List<String>,
        sourceLang: TranslateLang,
        targetLang: TranslateLang,
        sessionId: String
    ): Resource<List<String>>
}