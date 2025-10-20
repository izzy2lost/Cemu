package com.izzy2lost.weeu.settings.general

import android.content.Context
import androidx.lifecycle.ViewModel
import com.izzy2lost.weeu.common.settings.EmulationSettings
import com.izzy2lost.weeu.common.settings.GuiSettings
import com.izzy2lost.weeu.common.settings.SettingsManager
import com.izzy2lost.weeu.common.ui.localization.getAvailableLanguages

class GeneralSettingsViewModel : ViewModel() {
    val languages: List<String>
    val languageToDisplayNameMap: Map<String, String>
    val emulationSettings: EmulationSettings = SettingsManager.emulationSettings
    val guiSettings: GuiSettings = SettingsManager.guiSettings

    init {
        val availableLanguages = getAvailableLanguages()
        languages = availableLanguages.map { it.code }
        languageToDisplayNameMap = availableLanguages.associateBy({ it.code }, { it.displayName })
    }

    fun setLanguage(language: String, context: Context) {
        com.izzy2lost.weeu.common.ui.localization.setLanguage(language, context)
        guiSettings.language = language
    }
}