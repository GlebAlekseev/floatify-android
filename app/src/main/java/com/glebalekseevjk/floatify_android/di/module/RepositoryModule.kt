package com.glebalekseevjk.floatify_android.di.module

import com.glebalekseevjk.floatify_android.data.repository.YandexOCRRepositoryImpl
import com.glebalekseevjk.floatify_android.data.repository.YandexTranslateRepositoryImpl
import com.glebalekseevjk.floatify_android.domain.repository.OCRRepository
import com.glebalekseevjk.floatify_android.domain.repository.TranslateRepository
import com.glebalekseevjk.premierleaguefixtures.di.scope.AppComponentScope
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @AppComponentScope
    @Binds
    fun bindOCRRepository(yandexOCRRepositoryImpl: YandexOCRRepositoryImpl): OCRRepository

    @AppComponentScope
    @Binds
    fun bindTranslateRepository(yandexTranslateRepositoryImpl: YandexTranslateRepositoryImpl): TranslateRepository
}