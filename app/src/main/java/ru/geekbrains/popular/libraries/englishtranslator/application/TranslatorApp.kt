package ru.geekbrains.popular.libraries.englishtranslator.application

import android.app.Application
import ru.geekbrains.popular.libraries.englishtranslator.di.AppComponent
import ru.geekbrains.popular.libraries.englishtranslator.di.DaggerAppComponent

class TranslatorApp: Application() {
    companion object {
        lateinit var instance: TranslatorApp
    }

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        component = DaggerAppComponent.builder()
            .setContext(this)
            .build()
    }
}