package com.izzy2lost.weeu.settings.input

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.dropUnlessResumed
import com.izzy2lost.weeu.common.ui.components.Button
import com.izzy2lost.weeu.common.ui.components.ScreenContent
import com.izzy2lost.weeu.common.ui.localization.controllerTypeToString
import com.izzy2lost.weeu.common.ui.localization.tr
import com.izzy2lost.weeu.nativeinterface.NativeInput

data class InputSettingsScreenActions(
    val goToInputOverlaySettings: () -> Unit,
    val goToControllerSettings: (controllerIndex: Int) -> Unit,
)

@Composable
fun InputSettingsScreen(navigateBack: () -> Unit, actions: InputSettingsScreenActions) {
    val controllers = remember {
        (0..<NativeInput.MAX_CONTROLLERS).map { controllerIndex ->
            controllerIndex to getControllerType(controllerIndex)
        }
    }
    ScreenContent(
        appBarText = tr("Input settings"),
        navigateBack = navigateBack,
    ) {
        Button(
            label = tr("Input overlay settings"),
            onClick = dropUnlessResumed { actions.goToInputOverlaySettings() },
        )
        controllers.forEach { (controllerIndex, controllerEmulatedType) ->
            Button(
                label = tr("Controller {0}", controllerIndex + 1),
                description = tr(
                    "Emulated controller: {0}",
                    controllerTypeToString(controllerEmulatedType)
                ),
                onClick = dropUnlessResumed { actions.goToControllerSettings(controllerIndex) },
            )
        }
    }
}

fun getControllerType(index: Int): Int =
    if (NativeInput.isControllerDisabled(index))
        NativeInput.EmulatedControllerType.DISABLED
    else NativeInput.getControllerType(index)
