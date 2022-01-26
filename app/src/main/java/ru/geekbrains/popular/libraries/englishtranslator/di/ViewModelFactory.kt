package ru.geekbrains.popular.libraries.englishtranslator.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.geekbrains.popular.libraries.englishtranslator.R
import ru.geekbrains.popular.libraries.englishtranslator.utils.resources.ResourcesProviderImpl
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(
    private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>,
    private val resourcesProviderImpl: ResourcesProviderImpl
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = viewModels[modelClass]
            ?: viewModels.asIterable().firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
            ?: throw IllegalArgumentException("${
                resourcesProviderImpl.getString(R.string.error_unknown_model_class)}: $modelClass")
        return try {
            creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}