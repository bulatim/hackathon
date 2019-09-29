package com.hack.qrpay.di.component

import com.hack.qrpay.di.module.AndroidModule
import com.hack.qrpay.di.module.ApplicationModule
import com.hack.qrpay.view.activity.MainActivity
import com.hack.qrpay.view.fragment.MainFragment
import com.hack.qrpay.view.fragment.QrGenerateFragment
import com.hack.qrpay.view.fragment.ServiceFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        ApplicationModule::class, AndroidModule::class))
interface AppComponent {
    fun inject(fragment: MainFragment)

    fun inject(fragment: QrGenerateFragment)

    fun inject(activity: MainActivity)

    fun inject(fragment: ServiceFragment)
}