package ru.geekbrains.popular.libraries.englishtranslator.di

import dagger.Module
import dagger.Provides
import ru.geekbrains.popular.libraries.englishtranslator.application.Constants
import ru.geekbrains.popular.libraries.englishtranslator.model.data.DataModel
import ru.geekbrains.popular.libraries.englishtranslator.model.datasource.DataSource
import ru.geekbrains.popular.libraries.englishtranslator.model.datasource.RetrofitImplementation
import ru.geekbrains.popular.libraries.englishtranslator.model.datasource.RoomDataBaseImplementation
import ru.geekbrains.popular.libraries.englishtranslator.model.repository.Repository
import ru.geekbrains.popular.libraries.englishtranslator.model.repository.RepositoryImplementation
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepositoryModule {

//    @Provides
//    @Singleton
//    @Named(NAME_REMOTE)
//    internal fun provideRepositoryRemote(@Named(NAME_REMOTE) dataSourceRemote:
//         DataSource<List<DataModel>>) = RepositoryImplementation(dataSourceRemote)
    @Provides
    @Singleton
    @Named(NAME_REMOTE)
    internal fun provideRepositoryRemote(@Named(NAME_REMOTE) dataSourceRemote:
        DataSource<List<DataModel>>):
        Repository<List<DataModel>> = RepositoryImplementation(dataSourceRemote)

//    @Provides
//    @Singleton
//    @Named(NAME_LOCAL)
//    internal fun provideRepositoryLocal(@Named(NAME_LOCAL)
//        dataSourceLocal: DataSource<List<DataModel>>) = RepositoryImplementation(dataSourceLocal)
    @Provides
    @Singleton
    @Named(NAME_LOCAL)
    internal fun provideRepositoryLocal(@Named(NAME_LOCAL) dataSourceLocal:
        DataSource<List<DataModel>>):
        Repository<List<DataModel>> = RepositoryImplementation(dataSourceLocal)

    @Provides
    @Singleton
    @Named(NAME_REMOTE)
    internal fun provideDataSourceRemote(): DataSource<List<DataModel>> = RetrofitImplementation()

    @Provides
    @Singleton
    @Named(NAME_LOCAL)
    internal fun provideDataSourceLocal(): DataSource<List<DataModel>> = RoomDataBaseImplementation()
}