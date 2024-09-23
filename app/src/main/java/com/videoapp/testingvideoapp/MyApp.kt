package com.videoapp.testingvideoapp

import android.app.Application
import com.videoapp.testingvideoapp.di.appModule
import com.videoapp.testingvideoapp.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {

            androidLogger(Level.DEBUG)
            androidContext(this@MyApp)

            modules(listOf(appModule, dataModule))
        }
    }
}