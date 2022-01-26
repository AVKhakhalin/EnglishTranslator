package ru.geekbrains.popular.libraries.englishtranslator.di

import dagger.Module
import dagger.Provides
import ru.geekbrains.popular.libraries.englishtranslator.application.Constants
import ru.geekbrains.popular.libraries.englishtranslator.model.data.DataModel
import ru.geekbrains.popular.libraries.englishtranslator.model.repository.Repository
import ru.geekbrains.popular.libraries.englishtranslator.utils.network.NetworkStatus
import ru.geekbrains.popular.libraries.englishtranslator.view.main.MainInteractor
import javax.inject.Named

@Module
class InteractorModule {

    @Provides
    internal fun provideInteractor(
        @Named(Constants.NAME_REMOTE) repositoryRemote: Repository<List<DataModel>>,
        @Named(Constants.NAME_LOCAL) repositoryLocal: Repository<List<DataModel>>,
        networkStatus: NetworkStatus
    ) = MainInteractor(repositoryRemote, repositoryLocal, networkStatus)
}