package ru.geekbrains.popular.libraries.englishtranslator.di

import dagger.BindsInstance
import dagger.Component
import ru.geekbrains.popular.libraries.englishtranslator.view.main.MainActivity
import javax.inject.Singleton

@Component(
    modules = [
        InteractorModule::class,
        RepositoryModule::class,
        ViewModelModule::class
    ]
)
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun appName(appName: String): Builder

        fun build(): AppComponent
    }

    fun inject(activity: MainActivity)
}