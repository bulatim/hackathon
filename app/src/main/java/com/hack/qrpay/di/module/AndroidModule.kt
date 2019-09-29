package com.hack.qrpay.di.module

import androidx.browser.customtabs.CustomTabsIntent

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class AndroidModule {

    @Provides
    @Singleton
    fun provideCustomTabsBuilder(): CustomTabsIntent.Builder = CustomTabsIntent.Builder()

    @Provides
    @Singleton
    fun provideCustomTabsIntent(builder: CustomTabsIntent.Builder): CustomTabsIntent = builder.build()
    
}
