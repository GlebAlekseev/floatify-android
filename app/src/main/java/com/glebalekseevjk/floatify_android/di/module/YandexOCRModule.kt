package com.glebalekseevjk.floatify_android.di.module

import android.content.Context
import com.glebalekseevjk.floatify_android.R
import com.glebalekseevjk.floatify_android.data.remote.yandex_ocr.YandexOCRService
import com.glebalekseevjk.premierleaguefixtures.di.scope.AppComponentScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier

@Module
interface YandexOCRModule {
    companion object {
        @AppComponentScope
        @YandexOCR
        @Provides
        fun provideRetrofitBuilder(context: Context): Retrofit.Builder =
            Retrofit.Builder()
                .baseUrl(context.resources.getString(R.string.yandex_ocr_url))
                .addConverterFactory(GsonConverterFactory.create())

        @AppComponentScope
        @Provides
        fun provideYandexOCRService(
            @YandexOCR retrofitBuilder: Builder
        ): YandexOCRService = retrofitBuilder.build().create(YandexOCRService::class.java)

        @Qualifier
        @Retention(AnnotationRetention.RUNTIME)
        annotation class YandexOCR
    }
}