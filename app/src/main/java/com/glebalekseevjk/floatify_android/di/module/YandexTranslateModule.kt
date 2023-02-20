package com.glebalekseevjk.floatify_android.di.module

import android.content.Context
import com.glebalekseevjk.floatify_android.R
import com.glebalekseevjk.floatify_android.data.remote.yandex_translate.YandexTranslateSessionRefreshInterceptor
import com.glebalekseevjk.floatify_android.data.remote.yandex_translate.YandexTranslateService
import com.glebalekseevjk.premierleaguefixtures.di.scope.AppComponentScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier

@Module
interface YandexTranslateModule {
    companion object {
        @YandexTranslate
        @Provides
        fun provideOkHttpClient(
            yandexTranslateSessionRefreshInterceptor: YandexTranslateSessionRefreshInterceptor
        ): OkHttpClient = OkHttpClient
            .Builder()
            .connectTimeout(200, TimeUnit.MILLISECONDS)
                // addInterceptor is needed to be able to spoof the response
            .addInterceptor(yandexTranslateSessionRefreshInterceptor)
            .build()

        @AppComponentScope
        @YandexTranslate
        @Provides
        fun provideRetrofitBuilder(
            context: Context
        ): Retrofit.Builder =
            Retrofit.Builder()
                .baseUrl(context.resources.getString(R.string.yandex_translate_url))
                .addConverterFactory(GsonConverterFactory.create())

        @AppComponentScope
        @Provides
        fun provideYandexTranslateService(
            @YandexTranslate retrofitBuilder: Builder,
            @YandexTranslate okHttpClient: OkHttpClient
        ): YandexTranslateService =
            retrofitBuilder
                .client(okHttpClient)
                .build()
                .create(YandexTranslateService::class.java)

        @Qualifier
        @Retention(AnnotationRetention.RUNTIME)
        annotation class YandexTranslate
    }
}