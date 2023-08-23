package com.glebalekseevjk.floatify_android.data.paddle.paddle_text_detect

import com.glebalekseevjk.floatify_android.domain.entity.Resource
import com.glebalekseevjk.floatify_android.domain.entity.ocr.OCRLang
import com.glebalekseevjk.floatify_android.domain.repository.TextDetectRepository
import java.io.File

class PaddleTextDetectRepositoryImpl: TextDetectRepository {
    override suspend fun detectText(imageFile: File, recognizedLang: OCRLang): Resource<String> {
        TODO("Not yet implemented")
    }
}