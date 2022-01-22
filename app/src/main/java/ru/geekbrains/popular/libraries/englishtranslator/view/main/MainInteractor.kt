package ru.geekbrains.popular.libraries.englishtranslator.view.main

import io.reactivex.Observable
import ru.geekbrains.popular.libraries.englishtranslator.application.Constants
import ru.geekbrains.popular.libraries.englishtranslator.model.data.AppState
import ru.geekbrains.popular.libraries.englishtranslator.model.data.DataModel
import ru.geekbrains.popular.libraries.englishtranslator.model.repository.Repository
import ru.geekbrains.popular.libraries.englishtranslator.viewmodel.Interactor
import java.util.regex.Matcher
import java.util.regex.Pattern

class MainInteractor(
    private val remoteRepository: Repository<List<DataModel>>,
    private val localRepository: Repository<List<DataModel>>
): Interactor<AppState> {

    /** Задание переменных */ //region
    // Переменные для распознавания языка вводимого слова для перевода
    private val engPattern: Pattern = Pattern.compile(Constants.ENGLISH_SYMBOLS)
    private var engMatcher: Matcher = engPattern.matcher("")
    private var isEnglishText: Boolean = true
    //endregion

    override fun getData(word: String, fromRemoteSource: Boolean): Observable<AppState> {
        // Определение языка (английский - true, русский - false) вводимого слова
        engMatcher = engPattern.matcher(word)
        isEnglishText = engMatcher.find()
        // Отображение полученного поискового запроса
        return if (fromRemoteSource) {
            remoteRepository.getData(word).map { AppState.Success(it, isEnglishText) }
        } else {
            localRepository.getData(word).map { AppState.Success(it, isEnglishText) }
        }
    }
}