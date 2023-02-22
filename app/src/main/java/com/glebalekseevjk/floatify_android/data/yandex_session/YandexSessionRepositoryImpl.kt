package com.glebalekseevjk.floatify_android.data.yandex_session

import android.content.Context
import android.content.Intent
import android.webkit.CookieManager
import com.glebalekseevjk.floatify_android.R
import com.glebalekseevjk.floatify_android.domain.repository.YandexSessionRepository
import com.glebalekseevjk.floatify_android.presentation.YandexSessionWebViewActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject


class YandexSessionRepositoryImpl @Inject constructor(
    private val context: Context
) : YandexSessionRepository {
    @Inject
    lateinit var baseOkHttpClient: OkHttpClient

    private val mutex = Mutex(locked = false)
    private var isGood = false

    override suspend fun refreshYandexSessionId(): String? = withContext(Dispatchers.IO) {
        var sessionId = getYandexSessionId()
        if (sessionId == "") {
            refreshYandexCookieViaWebView()
            mutex.withLock {
                if (!isGood) return@withContext null
                sessionId = getYandexSessionId()
                if (sessionId == "") sessionId = null
            }
        }
        sessionId
    }

    override fun yandexSessionWebViewActivityStopped(isGood: Boolean) {
        if (mutex.isLocked) {
            this.isGood = isGood
            mutex.unlock()
        }
    }

    private fun getYandexSessionId(): String? {
        val cookies: String = try {
            CookieManager.getInstance()
                .getCookie(context.resources.getString(R.string.yandex_session_url))
        } catch (e: Exception) {
            return ""
        }
        val request = Request.Builder()
            .url(context.resources.getString(R.string.yandex_session_url))
            .method("GET", null)
            .addHeader("Cookie", cookies)
            .build()
        val response = baseOkHttpClient.newCall(request).execute()
        when (response.code()) {
            200 -> {}
            else -> return null
        }
        val htmlText = response.body()!!.string()
        val matcher =
            "(Ya.reqid = '){1}[a-z0-9]*(.){1}[a-z0-9]*(.){1}[a-z0-9]*(.){1}[a-z0-9]*(';){1}".toRegex()
        matcher.find(htmlText)?.let { return it.value.slice(12..it.value.length - 3) + "-0-0" }
        return ""
    }

    private suspend fun refreshYandexCookieViaWebView() {
        val intent = Intent(context, YandexSessionWebViewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        mutex.lock()
    }
}