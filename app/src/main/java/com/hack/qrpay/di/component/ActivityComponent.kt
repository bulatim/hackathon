package com.hack.qrpay.di.component

import com.hack.qrpay.di.PerActivity
import com.hack.qrpay.di.module.ActivityModule
import dagger.Component

@PerActivity
@Component(modules = arrayOf(ActivityModule::class))
interface ActivityComponent
