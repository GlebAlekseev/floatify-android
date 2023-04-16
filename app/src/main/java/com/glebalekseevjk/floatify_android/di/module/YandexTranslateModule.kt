package com.glebalekseevjk.floatify_android.di.module

import android.content.Context
import com.glebalekseevjk.floatify_android.R
import com.glebalekseevjk.floatify_android.data.yandex_public.yandex_translate.YandexTranslateService
import com.glebalekseevjk.premierleaguefixtures.di.scope.AppComponentScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier

@Module
interface YandexTranslateModule {
    companion object {
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
            @YandexTranslate retrofitBuilder: Builder
        ): YandexTranslateService =
            retrofitBuilder
                .build()
                .create(YandexTranslateService::class.java)

        @Qualifier
        @Retention(AnnotationRetention.RUNTIME)
        annotation class YandexTranslate
    }
}