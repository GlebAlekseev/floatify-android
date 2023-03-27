package com.glebalekseevjk.floatify_android.domain.entity.ocr

sealed class OCRLang {
    object ANY : OCRLang()
    object RU : OCRLang()
    object EN : OCRLang()
}
