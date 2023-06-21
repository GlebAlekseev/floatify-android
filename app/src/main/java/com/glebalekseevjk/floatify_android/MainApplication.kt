package com.glebalekseevjk.floatify_android

import android.app.Application
import android.content.Context
import com.glebalekseevjk.floatify_android.di.AppComponent
import com.glebalekseevjk.floatify_android.di.DaggerAppComponent

class MainApplication : Application() {
    companion object {
        lateinit var instance: Application
    }
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent.injectMainApplication(this)
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is MainApplication -> {
            appComponent
        }
        else -> {
            this.applicationContext.appComponent
        }
    }