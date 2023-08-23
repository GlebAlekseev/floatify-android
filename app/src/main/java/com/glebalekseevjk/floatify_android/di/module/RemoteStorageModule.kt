package com.glebalekseevjk.floatify_android.di.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

@Module
interface RemoteStorageModule {
    companion object {
        @Provides
        fun provideOkHttpClient(): OkHttpClient = OkHttpClient
            .Builder()
            .connectTimeout(200, TimeUnit.MILLISECONDS)
            .build()
    }
}