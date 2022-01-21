package ru.geekbrains.popular.libraries.englishtranslator.viewmodel

import io.reactivex.Observable

interface Interactor<T> {

    fun getData(word: String, fromRemoteSource: Boolean): Observable<T>
}