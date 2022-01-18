package ru.geekbrains.popular.libraries.englishtranslator.model.datasource

import io.reactivex.Observable
import ru.geekbrains.popular.libraries.englishtranslator.model.data.DataModel

class DataSourceRemote(private val remoteProvider:
                       RetrofitImplementation = RetrofitImplementation()):
    DataSource<List<DataModel>> {

    override fun getData(word: String): Observable<List<DataModel>> = remoteProvider.getData(word)
}