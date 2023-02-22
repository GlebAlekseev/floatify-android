package com.glebalekseevjk.floatify_android.presentation

import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.glebalekseevjk.floatify_android.R
import com.glebalekseevjk.floatify_android.appComponent
import com.glebalekseevjk.floatify_android.databinding.ActivityYandexSessionWebViewBinding
import com.glebalekseevjk.floatify_android.domain.interactor.YandexSessionUseCase
import javax.inject.Inject

class YandexSessionWebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityYandexSessionWebViewBinding

    @Inject
    lateinit var yandexSessionUseCase: YandexSessionUseCase

    private var isGood = false

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.createYandexSessionWebViewActivitySubcomponent().inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_yandex_session_web_view
        )
        setContentView(binding.root)
        val webView = binding.webview
        webView.settings.loadsImagesAutomatically = true
        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptEnabled = true
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if (request?.url.toString().contains("https://translate.yandex.ru/ocr")) {
                    isGood = true
                    finish()
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        webView.loadUrl(resources.getString(R.string.yandex_session_url))
    }

    override fun onDestroy() {
        super.onDestroy()
        yandexSessionUseCase.yandexSessionWebViewActivityStopped(isGood)
    }
}