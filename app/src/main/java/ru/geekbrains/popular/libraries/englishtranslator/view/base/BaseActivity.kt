package ru.geekbrains.popular.libraries.englishtranslator.view.base

import androidx.appcompat.app.AppCompatActivity
import ru.geekbrains.popular.libraries.englishtranslator.model.data.AppState
import ru.geekbrains.popular.libraries.englishtranslator.viewmodel.BaseViewModel
import ru.geekbrains.popular.libraries.englishtranslator.viewmodel.Interactor

abstract class BaseActivity<T: AppState, I: Interactor<T>>: AppCompatActivity() {

    abstract val model: BaseViewModel<T>

//    abstract fun renderData(appState: T)
    abstract fun renderData(dataModel: T)
}