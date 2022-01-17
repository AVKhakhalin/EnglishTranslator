package ru.geekbrains.popular.libraries.englishtranslator.model.datasource

import io.reactivex.Observable
import ru.geekbrains.popular.libraries.englishtranslator.model.data.DataModel

class RoomDataBaseImplementation : DataSource<List<DataModel>> {

    override fun getData(word: String): Observable<List<DataModel>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}