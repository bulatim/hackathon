package com.hack.qrpay

import android.app.Application
import android.content.IntentFilter
import com.hack.qrpay.di.component.AppComponent
import com.hack.qrpay.di.component.DaggerAppComponent
import com.hack.qrpay.di.module.ApplicationModule
import com.hack.qrpay.receiver.IncomingSMSReceiver

open class MainApplication : Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        createComponent()
    }

    protected open fun createComponent() {
        component = DaggerAppComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }
}

