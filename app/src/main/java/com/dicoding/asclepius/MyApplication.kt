package com.dicoding.asclepius

import android.app.Application
import com.dicoding.asclepius.di.databaseModule
import com.dicoding.asclepius.di.networkModule
import com.dicoding.asclepius.di.repositoryModule
import com.dicoding.asclepius.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    viewModelModule,
                )
            )
        }
    }
}