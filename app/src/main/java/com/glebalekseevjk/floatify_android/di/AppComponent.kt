package com.glebalekseevjk.floatify_android.di

import android.content.Context
import com.glebalekseevjk.floatify_android.MainApplication
import com.glebalekseevjk.floatify_android.di.module.RepositoryModule
import com.glebalekseevjk.floatify_android.di.module.YandexOCRModule
import com.glebalekseevjk.premierleaguefixtures.di.scope.AppComponentScope
import dagger.BindsInstance
import dagger.Component

@AppComponentScope
@Component(modules = [RepositoryModule::class, YandexOCRModule::class])
interface AppComponent {
    fun injectMainApplication(application: MainApplication)
    fun createMainActivitySubcomponent(): MainActivitySubcomponent

    @Component.Factory
    interface Builder {
        fun create(@BindsInstance context: Context): AppComponent
    }
}