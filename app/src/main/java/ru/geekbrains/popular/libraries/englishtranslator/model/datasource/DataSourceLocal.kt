package ru.geekbrains.popular.libraries.englishtranslator.model.datasource

import io.reactivex.Observable
import ru.geekbrains.popular.libraries.englishtranslator.model.data.DataModel

class DataSourceLocal(private val remoteProvider:
                      RoomDataBaseImplementation = RoomDataBaseImplementation()):
    DataSource<List<DataModel>> {

    override fun getData(word: String): Observable<List<DataModel>> = remoteProvider.getData(word)
}