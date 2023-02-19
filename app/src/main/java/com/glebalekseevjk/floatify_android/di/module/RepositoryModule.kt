package com.glebalekseevjk.floatify_android.di.module

import com.glebalekseevjk.floatify_android.data.repository.YandexOCRRepositoryImpl
import com.glebalekseevjk.floatify_android.domain.repository.OCRRepository
import com.glebalekseevjk.premierleaguefixtures.di.scope.AppComponentScope
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @AppComponentScope
    @Binds
    fun bindOCRRepository(yandexOCRRepositoryImpl: YandexOCRRepositoryImpl): OCRRepository
}