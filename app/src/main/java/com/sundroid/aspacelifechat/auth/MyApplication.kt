package com.sundroid.aspacelifechat.auth



import android.app.Application
import com.sundroid.aspacelifechat.di.appModule
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }
}