package com.izzy2lost.weeu.settings.inputoverlay

import androidx.lifecycle.ViewModel
import com.izzy2lost.weeu.common.settings.SettingsManager

class InputOverlaySettingsViewModel : ViewModel() {
    val overlaySettings = SettingsManager.inputOverlaySettings
}