package com.glebalekseevjk.floatify_android.data.paddle.paddle_ocr

import com.glebalekseevjk.floatify_android.domain.entity.Resource
import com.glebalekseevjk.floatify_android.domain.entity.ocr.OCRLang
import com.glebalekseevjk.floatify_android.domain.repository.OCRRepository
import java.io.File

class PaddleOCRRepositoryImpl: OCRRepository {
    override suspend fun recognizeText(imageFile: File, recognizedLang: OCRLang): Resource<String> {
        TODO("Not yet implemented")
    }

}