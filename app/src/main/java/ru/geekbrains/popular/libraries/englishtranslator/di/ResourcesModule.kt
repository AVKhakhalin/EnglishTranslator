package ru.geekbrains.popular.libraries.englishtranslator.di

import dagger.Module
import dagger.Provides
import ru.geekbrains.popular.libraries.englishtranslator.utils.resources.ResourcesProvider
import ru.geekbrains.popular.libraries.englishtranslator.utils.resources.ResourcesProviderImpl
import ru.geekbrains.popular.libraries.englishtranslator.view.utils.ThemeColors
import ru.geekbrains.popular.libraries.englishtranslator.view.utils.ThemeColorsImpl
import javax.inject.Singleton

@Module
class ResourcesModule {

    @Singleton
    @Provides
    fun resProvider(resourcesProviderImpl: ResourcesProviderImpl): ResourcesProvider {
        return resourcesProviderImpl
    }

    @Singleton
    @Provides
    fun themeColorsProvider(themeColorsImpl: ThemeColorsImpl): ThemeColors {
        return themeColorsImpl
    }
}