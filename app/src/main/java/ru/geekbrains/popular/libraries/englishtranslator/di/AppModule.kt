package ru.geekbrains.popular.libraries.englishtranslator.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.geekbrains.popular.libraries.englishtranslator.application.TranslatorApp
import javax.inject.Singleton

@Module
class AppModule(private val app: TranslatorApp) {

    @Singleton
    @Provides
    fun context(): Context {
        return app
    }

    @Singleton
    @Provides
    fun app(): TranslatorApp {
        return app
    }
}