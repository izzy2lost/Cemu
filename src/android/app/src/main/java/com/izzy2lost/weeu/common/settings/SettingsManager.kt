package com.izzy2lost.weeu.common.settings

import android.content.Context
import android.content.SharedPreferences
import com.izzy2lost.weeu.common.ui.localization.DEFAULT_LANGUAGE
import kotlin.getValue

enum class GamePadPosition {
    ABOVE,
    BELOW,
    LEFT,
    RIGHT;

    fun isVertical() = this == ABOVE || this == BELOW
    fun appearsAfterTV() = this == BELOW || this == RIGHT
}

class EmulationSettings(sharedPreferences: SharedPreferences) {
    var gamePadPosition by sharedPreferences.enumPref(GamePadPosition.RIGHT)
}

enum class GameListViewMode {
    LIST,
    COVER
}

class GuiSettings(sharedPreferences: SharedPreferences) {
    var language by sharedPreferences.stringPref(DEFAULT_LANGUAGE)
    var gameListViewMode by sharedPreferences.enumPref(GameListViewMode.LIST)
}

class InputOverlaySettings(sharedPreferences: SharedPreferences) {
    var isVibrateOnTouchEnabled by sharedPreferences.booleanPref(false)
    var isOverlayEnabled by sharedPreferences.booleanPref(true)
    var controllerIndex by sharedPreferences.intPref(0)
    var alpha by sharedPreferences.intPref(64)
}

object SettingsManager {
    fun initialize(context: Context) {
        sharedPreferences =
            context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE)
    }

    private lateinit var sharedPreferences: SharedPreferences

    val emulationSettings by lazy { EmulationSettings(sharedPreferences) }

    val guiSettings by lazy { GuiSettings(sharedPreferences) }

    val inputOverlaySettings by lazy { InputOverlaySettings(sharedPreferences) }

    private const val SETTINGS_NAME = "SETTINGS"
}
