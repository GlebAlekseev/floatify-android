package com.glebalekseevjk.floatify_android.domain.interactor

import com.glebalekseevjk.floatify_android.domain.entity.ocr.OCRLang
import com.glebalekseevjk.floatify_android.domain.repository.OCRRepository
import java.io.File
import javax.inject.Inject

class OCRUseCase @Inject constructor(private val ocrRepository: OCRRepository) {
    suspend fun recognizeText(imageFile: File, recognizedLang: OCRLang) =
        ocrRepository.recognizeText(imageFile, recognizedLang)
}