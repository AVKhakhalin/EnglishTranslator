package ru.geekbrains.popular.libraries.englishtranslator.view.main

import android.animation.ObjectAnimator
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.activity_main.view.*
import ru.geekbrains.popular.libraries.englishtranslator.Constants
import ru.geekbrains.popular.libraries.englishtranslator.R
import ru.geekbrains.popular.libraries.englishtranslator.databinding.ActivityMainBinding
import ru.geekbrains.popular.libraries.englishtranslator.model.data.AppState
import ru.geekbrains.popular.libraries.englishtranslator.model.data.DataModel
import ru.geekbrains.popular.libraries.englishtranslator.presenter.Presenter
import ru.geekbrains.popular.libraries.englishtranslator.view.base.BaseActivity
import ru.geekbrains.popular.libraries.englishtranslator.view.base.View
import ru.geekbrains.popular.libraries.englishtranslator.view.main.adapter.MainAdapter
import ru.geekbrains.popular.libraries.englishtranslator.view.utils.ThemeColor


class MainActivity : BaseActivity<AppState>() {
    /** Задание переменных */ //region
    // Binding
    private lateinit var binding: ActivityMainBinding
    // MainAdapter
    private var adapter: MainAdapter? = null
    // Bottom navigation menu (признако основного состояния Main State - когда можно вводить слова)
    private var isMain: Boolean = true
    // Установка темы приложения
    private var isThemeDay: Boolean = true
    // Цвета из аттрибутов темы
    private var themeColor: ThemeColor? = null
    // Событие: клик по элементу списка с найденными словами
    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object: MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                Toast.makeText(this@MainActivity, data.text, Toast.LENGTH_SHORT).show()
            }
        }
    //endregion


    override fun createPresenter(): Presenter<AppState, View> {
        return MainPresenterImpl()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Считывание системных настроек, применение темы к приложению
        readSettingsAndSetupApplication(savedInstanceState)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Получение цветов из аттрибутов темы
        themeColor = ThemeColor(theme)
        themeColor?.let { it.initiateColors() }

        // Установка события нажатия на нижниюю FAB для открытия и закрытия поискового элемента
        binding.bottomNavigationMenu.bottomAppBarFab.setOnClickListener {
            switchBottomAppBar()
        }
        // Начальная установка доступности поискового поля
        switchBottomAppBar()

        setContentView(binding.root)
    }

    override fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                val dataModel = appState.data
                if (dataModel == null || dataModel.isEmpty()) {
                    showErrorScreen(getString(R.string.empty_server_response_on_success))
                } else {
                    showViewSuccess()
                    if (adapter == null) {
                        binding.mainActivityRecyclerview.layoutManager =
                            LinearLayoutManager(applicationContext)
                        binding.mainActivityRecyclerview.adapter =
                            MainAdapter(onListItemClickListener, dataModel)
                    } else {
                        adapter?.let {
                            it.setData(dataModel)
                        }
                    }
                }
            }
            is AppState.Loading -> {
                showViewLoading()
                if (appState.progress != null) {
                    binding.progressBarHorizontal.visibility = VISIBLE
                    binding.progressBarRound.visibility = GONE
                    binding.progressBarHorizontal.progress = appState.progress
                } else {
                    binding.progressBarHorizontal.visibility = GONE
                    binding.progressBarRound.visibility = VISIBLE
                }
            }
            is AppState.Error -> {
                showErrorScreen(appState.error.message)
            }
        }
    }

    private fun showErrorScreen(error: String?) {
        showViewError()
        binding.errorTextview.text = error ?: getString(R.string.undefined_error)
        binding.reloadButton.setOnClickListener {
            presenter.getData(this.getString(R.string.error_textview_stub))
        }
    }

    private fun showViewSuccess() {
        binding.successLinearLayout.visibility = VISIBLE
        binding.loadingFrameLayout.visibility = GONE
        binding.errorLinearLayout.visibility = GONE
    }

    private fun showViewLoading() {
        binding.successLinearLayout.visibility = GONE
        binding.loadingFrameLayout.visibility = VISIBLE
        binding.errorLinearLayout.visibility = GONE
    }

    private fun showViewError() {
        binding.successLinearLayout.visibility = GONE
        binding.loadingFrameLayout.visibility = GONE
        binding.errorLinearLayout.visibility = VISIBLE
    }

    // Переключение режима нижней навигационной кнопки BottomAppBar
    // с центрального на крайнее правое положение и обратно
    private fun switchBottomAppBar() {
        if (isMain) {
            // Анимация вращения картинки на нижней кнопке FAB
            ObjectAnimator.ofFloat(
                binding.bottomNavigationMenu.bottomAppBarFab,
                "rotation", 0f, -Constants.ANGLE_TO_ROTATE_BOTTOM_FAB
            ).start()
            // Изменение нижней кнопки FAB
            isMain = false
            binding.bottomNavigationMenu.bottomAppBar.navigationIcon = null
            binding.bottomNavigationMenu.bottomAppBar.fabAlignmentMode =
                BottomAppBar.FAB_ALIGNMENT_MODE_END
            binding.bottomNavigationMenu.bottomAppBarFab.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_back_fab)
            )
            binding.bottomNavigationMenu.bottomAppBar.replaceMenu(
                R.menu.bottom_menu_bottom_bar_other_screen
            )

            //region НАСТРОЙКИ ПОИСКОВОГО ПОЛЯ
            // Установка поискового поля
            val searchViewActionView: android.view.View = binding.bottomNavigationMenu.bottomAppBar
                .menu.findItem(R.id.action_bottom_bar_search_request_form).actionView
            val searchView: SearchView = searchViewActionView as SearchView
            // Установка ранее заданного слова
            // TODO
//            searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text).setText("")
            // Событие установки поискового запроса
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    // Отображение полученного поискового запроса
                    Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                    presenter.getData(query)
                    return false
                }

                // Отслеживание появления каждого символа
                override fun onQueryTextChange(newText: String): Boolean {
                    // Отображение текущего поискового запроса
                    // TODO
                    return false
                }
            })

            // Событие на закрытие поискового окна (обнуление фильтра)
            searchView.setOnCloseListener {
                // TODO
//                Toast.makeText(this@MainActivity, "Close", Toast.LENGTH_SHORT).show()
                true
            }
            // Получение поискового поля для ввода и редактирования текста поискового
            val searchedEditText =
                searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            // Установка цветов фона и текста для поискового поля
            searchedEditText.setBackgroundResource(R.drawable.search_view_shape)
            themeColor?.let {
                searchedEditText.setTextColor(it.getColorTypedValue())
                searchedEditText.setHintTextColor(it.getColorTypedValue())
            }
            // Установка размера поискового текста
            searchedEditText.textSize = Constants.SEARCH_FIELD_TEXT_SIZE
            // Открытие поля для ввода текста
            searchView.isIconified = false
            // Анимированное появление поля для ввода текста
            ObjectAnimator.ofFloat(searchView, "alpha", 0F, 1F)
                .setDuration(1500).start()
            //endregion
        } else {
            // Закрытие поля для ввода текста
//            val searchViewActionView: android.view.View = binding.bottomNavigationMenu.bottomAppBar
//                .menu.findItem(R.id.action_bottom_bar_search_request_form).actionView
//            val searchView: SearchView = searchViewActionView as SearchView
//            searchView.onActionViewCollapsed()
//            // Скрытие поля для ввода текста
//            searchView.visibility = android.view.View.INVISIBLE

            // Анимация вращения картинки на нижней кнопке FAB
            ObjectAnimator.ofFloat(
                binding.bottomNavigationMenu.bottomAppBarFab,
                "rotation", 0f, Constants.ANGLE_TO_ROTATE_BOTTOM_FAB
            ).start()
            // Изменение нижней кнопки FAB
            isMain = true
            binding.bottomNavigationMenu.bottomAppBar.navigationIcon =
                ContextCompat.getDrawable(this, R.drawable.ic_hamburger_menu_bottom_bar)
            binding.bottomNavigationMenu.bottomAppBar.fabAlignmentMode =
                BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
            binding.bottomNavigationMenu.bottomAppBarFab.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_plus_fab)
            )
            // Появление меню с настройками приложения
            binding.bottomNavigationMenu.bottomAppBar
                .replaceMenu(R.menu.bottom_menu_navigation)
            // Установка события смены кнопки
            binding.bottomNavigationMenu.bottomAppBar.menu
                .getItem(Constants.BUTTON_CHANGE_THEME_INDEX).setOnMenuItemClickListener {
                    isMain = false
                    setIsThemeDay(!isThemeDay)
                    recreate()
                true
            }
            // Анимированное появление кнопки меню со сменой темы
//            ObjectAnimator.ofFloat(binding.bottomNavigationMenu.bottomAppBar.menu
//                .findItem(R.id.action_change_theme).actionView, "alpha", 0F, 1F)
//                .setDuration(1500).start()
        }
    }

    // Считывание системных настроек, применение темы к приложению
    private fun readSettingsAndSetupApplication(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val sharedPreferences: SharedPreferences =
                getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, MODE_PRIVATE)
            isThemeDay = sharedPreferences.getBoolean(
                Constants.SHARED_PREFERENCES_THEME_KEY, true)
            isMain = sharedPreferences.getBoolean(
                Constants.SHARED_PREFERENCES_MAIN_STATE_KEY, true)
            if (isThemeDay) {
                setTheme(R.style.DayTheme)
            } else {
                setTheme(R.style.NightTheme)
            }
        } else {
            // Применение тёмной темы при первом запуске приложения
            // на мобильных устройствах с версией Android 10+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                setTheme(R.style.NightTheme)
            }
        }
    }

    // Установка темы приложения
    private fun setIsThemeDay(isThemeDay: Boolean) {
        this.isThemeDay = isThemeDay
        saveApplicatonSettings()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        saveApplicatonSettings()
    }

    // Сохранение настроек приложения
    private fun saveApplicatonSettings() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, AppCompatActivity.MODE_PRIVATE)
        val sharedPreferencesEditor: SharedPreferences.Editor = sharedPreferences.edit()
        sharedPreferencesEditor.putBoolean(Constants.SHARED_PREFERENCES_THEME_KEY, isThemeDay)
        sharedPreferencesEditor.putBoolean(Constants.SHARED_PREFERENCES_MAIN_STATE_KEY, isMain)
        sharedPreferencesEditor.apply()
    }
}