package ru.geekbrains.popular.libraries.englishtranslator.application

import android.app.Application
import ru.geekbrains.popular.libraries.englishtranslator.di.AppComponent
import ru.geekbrains.popular.libraries.englishtranslator.di.DaggerAppComponent

class TranslatorApp: Application() {
    companion object {
        val component: AppComponent by lazy {
            // DaggerAppComponent создаётся при первой компиляции приложения
            DaggerAppComponent.builder().build()
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}