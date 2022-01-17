package ru.geekbrains.popular.libraries.englishtranslator.model.datasource

import io.reactivex.Observable

interface DataSource<T> {

    fun getData(word: String): Observable<T>
}