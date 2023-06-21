package com.glebalekseevjk.floatify_android.di

import com.glebalekseevjk.floatify_android.presentation.activity.YandexSessionWebViewActivity
import dagger.Subcomponent

@Subcomponent
interface YandexSessionWebViewActivitySubcomponent {
    fun inject(yandexSessionWebViewActivity: YandexSessionWebViewActivity)
}