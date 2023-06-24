package com.glebalekseevjk.floatify_android.data.free_translate

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FreeTranslateRetrofit {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://ftapi.pythonanywhere.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val translationApi: FreeTranslateService = retrofit.create(FreeTranslateService::class.java)
}