package com.glebalekseevjk.floatify_android.data.remote.yandex_translate.dto

data class YandexTranslateResponse(
    val code: Int,
    val lang: String,
    val text: List<String>
)
