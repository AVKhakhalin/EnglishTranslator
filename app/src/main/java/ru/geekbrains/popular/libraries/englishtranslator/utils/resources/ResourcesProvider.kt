package ru.geekbrains.popular.libraries.englishtranslator.utils.resources

import androidx.annotation.StringRes

interface ResourcesProvider {

    fun getString(@StringRes id: Int): String
}