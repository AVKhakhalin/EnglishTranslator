package ru.geekbrains.popular.libraries.englishtranslator.view.main

import androidx.lifecycle.LiveData
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import ru.geekbrains.popular.libraries.englishtranslator.model.data.AppState
import ru.geekbrains.popular.libraries.englishtranslator.model.datasource.DataSourceLocal
import ru.geekbrains.popular.libraries.englishtranslator.model.datasource.DataSourceRemote
import ru.geekbrains.popular.libraries.englishtranslator.model.repository.RepositoryImplementation
import ru.geekbrains.popular.libraries.englishtranslator.utils.parseSearchResults
import ru.geekbrains.popular.libraries.englishtranslator.viewmodel.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor (
    private val interactor: MainInteractor
): BaseViewModel<AppState>() {
    private var appState: AppState? = null

    fun subscribe(): LiveData<AppState> {
        return liveDataForViewToObserve
    }

    override fun getData(word: String, isOnline: Boolean) {
        compositeDisposable.add(
            interactor.getData(word, isOnline)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
//                .doOnSubscribe { liveDataForViewToObserve.value = AppState.Loading(null) }
                .doOnSubscribe(doOnSubscribe())
                .subscribeWith(getObserver())
        )
//        return super.getData(word, isOnline)
    }

    private fun doOnSubscribe(): (Disposable) -> Unit =
        { liveDataForViewToObserve.value = AppState.Loading(null)}

    private fun getObserver(): DisposableObserver<AppState> {
        return object: DisposableObserver<AppState>() {

            override fun onNext(state: AppState) {
//                appState = state
                appState = parseSearchResults(state)
                liveDataForViewToObserve.value = appState
            }

            override fun onError(e: Throwable) {
                liveDataForViewToObserve.value = AppState.Error(e)
            }

            override fun onComplete() {
            }
        }
    }
}