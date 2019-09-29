package com.hack.qrpay.di.module

import android.app.Activity
import android.content.Context

import com.hack.qrpay.di.ActivityContext
import com.hack.qrpay.di.PerActivity

import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: Activity) {

    @PerActivity
    @Provides
    @ActivityContext
    fun provideContext(): Context = activity
}
