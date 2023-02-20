package com.glebalekseevjk.floatify_android.data.remote.yandex_translate

import com.glebalekseevjk.floatify_android.data.preferences.SharedPreferencesTemporaryStorage
import okhttp3.*
import javax.inject.Inject


class YandexTranslateSessionRefreshInterceptor @Inject constructor(
    private val temporaryStorage: SharedPreferencesTemporaryStorage
) : Interceptor {
    @Inject
    lateinit var baseOkHttpClient: OkHttpClient

    override fun intercept(chain: Interceptor.Chain): Response {
        val sessionId = temporaryStorage.getYandexTranslateSessionId()
            ?: refreshYandexTranslateSessionId()
        if (sessionId == "") return Response
            .Builder()
            .code(400)
            .message("")
            .body(ResponseBody.create(null, ""))
            .protocol(Protocol.HTTP_2)
            .request(chain.request())
            .build()

        return chain.request()
            .addSessionIdQueryParameter(sessionId)
            .let { chain.proceed(it) }
    }

    private fun refreshYandexTranslateSessionId(): String {
        val request = Request.Builder()
            .url("https://translate.yandex.ru/ocr")
            .method("GET", null)
            .build()
        val response = baseOkHttpClient.newCall(request).execute()
        when(response.code()){
            200->{}
            else->return ""
        }
        val htmlText = response.body()!!.string()
        val matcher = "(Ya.reqid = '){1}[a-z0-9]*(.){1}[a-z0-9]*(.){1}[a-z0-9]*(.){1}[a-z0-9]*(';){1}".toRegex()
        val matchResult = matcher.find(htmlText) ?: return ""
        val sessionId = matchResult.value.slice(12..matchResult.value.length - 3) + "-0-0"
        temporaryStorage.setYandexTranslateSessionId(sessionId)
        return sessionId
    }

    private fun Request.addSessionIdQueryParameter(sessionId: String): Request {
        return newBuilder()
            .apply {
                val url = url()
                    .newBuilder()
                    .addQueryParameter("id", sessionId)
                    .build()
                url(url)
            }
            .build()
    }
}