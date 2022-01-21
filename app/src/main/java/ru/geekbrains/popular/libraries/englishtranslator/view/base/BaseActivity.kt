package ru.geekbrains.popular.libraries.englishtranslator.view.base

import androidx.appcompat.app.AppCompatActivity
import ru.geekbrains.popular.libraries.englishtranslator.model.data.AppState
import ru.geekbrains.popular.libraries.englishtranslator.viewmodel.BaseViewModel

abstract class BaseActivity<T: AppState>: AppCompatActivity() {

    abstract val model: BaseViewModel<T>

    abstract fun renderData(appState: T)
}