package ru.geekbrains.popular.libraries.englishtranslator.view.base

import ru.geekbrains.popular.libraries.englishtranslator.model.data.AppState

interface View {

    fun renderData(appState: AppState)

}