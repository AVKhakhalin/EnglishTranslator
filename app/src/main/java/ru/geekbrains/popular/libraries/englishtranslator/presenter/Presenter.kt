package ru.geekbrains.popular.libraries.englishtranslator.presenter

import ru.geekbrains.popular.libraries.englishtranslator.model.data.AppState
import ru.geekbrains.popular.libraries.englishtranslator.view.base.View

interface Presenter<T: AppState, V: View> {

    fun attachView(view: V)
    fun detachView(view: V)
    fun getData(word: String, isOnline: Boolean)
}