package com.glebalekseevjk.floatify_android.data.repository

import com.glebalekseevjk.floatify_android.data.remote.yandex_ocr.YandexOCRService
import com.glebalekseevjk.floatify_android.data.remote.yandex_ocr.dto.YandexOCRResponse
import com.glebalekseevjk.floatify_android.domain.entity.Resource
import com.glebalekseevjk.floatify_android.domain.entity.ocr.OCRLang
import com.glebalekseevjk.floatify_android.domain.repository.OCRRepository
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import javax.inject.Inject


class YandexOCRRepositoryImpl @Inject constructor(
    private val yandexOCRService: YandexOCRService
) : OCRRepository {
    override suspend fun recognizeText(
        imageFile: File,
        recognizedLang: OCRLang
    ): Resource<String> = with(Dispatchers.IO) {
        val options = YandexOCRService.baseOptions.toMutableMap()
        options["lang"] = when (recognizedLang) {
            OCRLang.ANY -> "*"
            OCRLang.EN -> "en"
            OCRLang.RU -> "ru"
        }

        val requestFile =
            RequestBody.create(MediaType.parse("image/jpeg"), imageFile)
        val body = MultipartBody.Part.createFormData("file", imageFile.name, requestFile);


        val response = yandexOCRService.recognizeText(options, body)
        return@with getResourceFromYandexOCRResponse(response)
    }

    private fun getResourceFromYandexOCRResponse(response: Response<YandexOCRResponse>): Resource<String> {
        response.code().let {
            when (it) {
                200 -> {}
                else -> return Resource.Failure<Nothing>(HttpException(response))
            }
        }
        val body = response.body()
            ?: return Resource.Failure<Nothing>(NullPointerException("response.body is null"))
        val stringBuilder = StringBuilder()
        body.data.blocks.forEach {
            it.boxes.forEach {
                stringBuilder.append(it.text)
            }
        }
        return Resource.Success(stringBuilder.toString())
    }
}