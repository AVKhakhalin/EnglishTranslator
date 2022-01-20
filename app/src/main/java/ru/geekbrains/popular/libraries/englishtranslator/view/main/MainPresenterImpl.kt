package ru.geekbrains.popular.libraries.englishtranslator.view.main

import android.content.Context
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import ru.geekbrains.popular.libraries.englishtranslator.model.data.AppState
import ru.geekbrains.popular.libraries.englishtranslator.model.datasource.DataSourceLocal
import ru.geekbrains.popular.libraries.englishtranslator.model.datasource.DataSourceRemote
import ru.geekbrains.popular.libraries.englishtranslator.model.repository.RepositoryImplementation
import ru.geekbrains.popular.libraries.englishtranslator.presenter.Presenter
import ru.geekbrains.popular.libraries.englishtranslator.rx.SchedulerProvider
import ru.geekbrains.popular.libraries.englishtranslator.view.base.View
import ru.geekbrains.popular.libraries.englishtranslator.view.utils.NetworkStatus

class MainPresenterImpl<T: AppState, V: View>(
    private val interactor: MainInteractor = MainInteractor(
        RepositoryImplementation(DataSourceRemote()),
        RepositoryImplementation(DataSourceLocal())
    ),
    protected val compositeDisposable: CompositeDisposable = CompositeDisposable(),
    protected val schedulerProvider: SchedulerProvider = SchedulerProvider()
): Presenter<T, V> {
    /** Задание переменных */ //region
    // Доступность сети
    private lateinit var networkStatus: NetworkStatus
    // Вью для отображения результата
    private var currentView: V? = null
    //endregion

    override fun attachView(view: V) {
        if (view != currentView) {
            currentView = view
            networkStatus = NetworkStatus(currentView as Context)
        }
    }

    override fun detachView(view: V) {
        compositeDisposable.clear()
        if (view == currentView) {
            currentView = null
        }
    }

    override fun getData(word: String) {
        compositeDisposable.add(
            interactor.getData(word, networkStatus.isOnline())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { currentView?.renderData(AppState.Loading(null)) }
                .subscribeWith(getObserver())
        )
    }

    private fun getObserver(): DisposableObserver<AppState> {
        return object: DisposableObserver<AppState>() {

            override fun onNext(appState: AppState) {
                currentView?.renderData(appState)
            }

            override fun onError(e: Throwable) {
                currentView?.renderData(AppState.Error(e))
            }

            override fun onComplete() {
            }
        }
    }
}