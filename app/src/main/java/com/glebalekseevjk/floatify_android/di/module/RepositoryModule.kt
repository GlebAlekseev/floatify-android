package com.glebalekseevjk.floatify_android.di.module

import com.glebalekseevjk.floatify_android.data.temporary.TemporaryRepositoryImpl
import com.glebalekseevjk.floatify_android.data.yandex_public.yandex_ocr.YandexOCRRepositoryImpl
import com.glebalekseevjk.floatify_android.data.yandex_public.yandex_session.YandexSessionRepositoryImpl
import com.glebalekseevjk.floatify_android.data.yandex_public.yandex_translate.YandexTranslateRepositoryImpl
import com.glebalekseevjk.floatify_android.domain.repository.OCRRepository
import com.glebalekseevjk.floatify_android.domain.repository.TemporaryRepository
import com.glebalekseevjk.floatify_android.domain.repository.TranslateRepository
import com.glebalekseevjk.floatify_android.domain.repository.YandexSessionRepository
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

    @AppComponentScope
    @Binds
    fun bindTemporaryRepository(temporaryRepositoryImpl: TemporaryRepositoryImpl): TemporaryRepository

    @AppComponentScope
    @Binds
    fun bindYandexSessionRepository(yandexSessionRepositoryImpl: YandexSessionRepositoryImpl): YandexSessionRepository
}