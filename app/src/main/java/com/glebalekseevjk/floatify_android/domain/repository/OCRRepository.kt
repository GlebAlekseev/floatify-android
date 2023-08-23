package com.glebalekseevjk.floatify_android.domain.repository

import com.glebalekseevjk.floatify_android.domain.entity.Resource
import com.glebalekseevjk.floatify_android.domain.entity.ocr.OCRLang
import java.io.File

interface OCRRepository {
    suspend fun recognizeText(imageFile: File, recognizedLang: OCRLang): Resource<String>
}