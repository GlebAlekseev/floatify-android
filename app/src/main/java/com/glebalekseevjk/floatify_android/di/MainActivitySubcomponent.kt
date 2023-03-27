package com.glebalekseevjk.floatify_android.di

import com.glebalekseevjk.floatify_android.presentation.MainActivity
import dagger.Subcomponent

@Subcomponent
interface MainActivitySubcomponent {
    fun inject(mainActivity: MainActivity)
}