package com.izzy2lost.weeu

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.os.Bundle
import android.provider.DocumentsContract
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.izzy2lost.weeu.about.AboutCemuRoute
import com.izzy2lost.weeu.about.aboutCemuNavigation
import com.izzy2lost.weeu.common.ui.components.ActivityContent
import com.izzy2lost.weeu.common.ui.localization.TranslatableContent
import com.izzy2lost.weeu.common.ui.localization.tr
import com.izzy2lost.weeu.emulation.EmulationActivity
import com.izzy2lost.weeu.gamelist.GameListRoute
import com.izzy2lost.weeu.gamelist.gameListNavigation
import com.izzy2lost.weeu.graphicpacks.GraphicPacksRoute
import com.izzy2lost.weeu.graphicpacks.graphicPacksNavigation
import com.izzy2lost.weeu.nativeinterface.NativeActiveSettings
import com.izzy2lost.weeu.nativeinterface.NativeGameTitles.Game
import com.izzy2lost.weeu.nativeinterface.NativeSettings
import com.izzy2lost.weeu.provider.DocumentsProvider
import com.izzy2lost.weeu.settings.AccountSettingsRoute
import com.izzy2lost.weeu.settings.AudioSettingsRoute
import com.izzy2lost.weeu.settings.GeneralSettingsRoute
import com.izzy2lost.weeu.settings.GraphicsSettingsRoute
import com.izzy2lost.weeu.settings.InputSettingsRoute
import com.izzy2lost.weeu.settings.OverlaySettingsRoute
import com.izzy2lost.weeu.settings.SettingsRoute
import com.izzy2lost.weeu.settings.settingsNavigation
import com.izzy2lost.weeu.titlemanager.TitleManagerRoute
import com.izzy2lost.weeu.titlemanager.titleManagerNavigation
import java.io.File

import android.graphics.drawable.Icon as AndroidIcon


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TranslatableContent {
                ActivityContent {
                    MainNav()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        NativeSettings.saveSettings()
    }

    override fun onPause() {
        super.onPause()
        NativeSettings.saveSettings()
    }
}

@Composable
private fun MainNav() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = GameListRoute,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        gameListNavigation(
            navController = navController,
            startGame = { startGame(context, it) },
            createShortcut = { createShortcutForGame(context, it) },
            gameListToolBarActions = {
                GameListToolBarActionsMenu(
                    goToTitleManager = { navController.navigate(TitleManagerRoute) },
                    goToGraphicPacks = { navController.navigate(GraphicPacksRoute) },
                    goToAboutCemu = { navController.navigate(AboutCemuRoute) }
                )
            },
            goToGeneralSettings = { navController.navigate(GeneralSettingsRoute) },
            goToInputSettings = { navController.navigate(InputSettingsRoute) },
            goToGraphicsSettings = { navController.navigate(GraphicsSettingsRoute) },
            goToAudioSettings = { navController.navigate(AudioSettingsRoute) },
            goToOverlaySettings = { navController.navigate(OverlaySettingsRoute) },
            goToAccountSettings = { navController.navigate(AccountSettingsRoute) }
        )
        settingsNavigation(navController)
        titleManagerNavigation(navController)
        graphicPacksNavigation(navController)
        aboutCemuNavigation(navController)
    }
}

@Composable
private fun GameListToolBarActionsMenu(
    goToTitleManager: () -> Unit,
    goToGraphicPacks: () -> Unit,
    goToAboutCemu: () -> Unit,
) {
    var expandMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current

    @Composable
    fun DropdownMenuItem(onClick: () -> Unit, text: String) {
        DropdownMenuItem(
            onClick = {
                onClick()
                expandMenu = false
            },
            text = { Text(text) },
        )
    }
    IconButton(
        modifier = Modifier.padding(end = 8.dp),
        onClick = { expandMenu = true },
    ) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = null
        )
    }
    DropdownMenu(
        expanded = expandMenu,
        onDismissRequest = { expandMenu = false },
        containerColor = MaterialTheme.colorScheme.secondaryContainer
    ) {
        DropdownMenuItem(
            onClick = goToGraphicPacks,
            text = { Text(tr("Graphic packs")) },
            colors = MenuDefaults.itemColors(
                textColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        )
        DropdownMenuItem(
            onClick = goToTitleManager,
            text = { Text(tr("Title manager")) },
            colors = MenuDefaults.itemColors(
                textColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        )
        DropdownMenuItem(
            onClick = { openCemuFolder(context) },
            text = { Text(tr("Open Cemu folder")) },
            colors = MenuDefaults.itemColors(
                textColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        )
        DropdownMenuItem(
            onClick = { shareLogFile(context) },
            text = { Text(tr("Share log file")) },
            colors = MenuDefaults.itemColors(
                textColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        )
        DropdownMenuItem(
            onClick = goToAboutCemu,
            text = { Text(tr("About Cemu")) },
            colors = MenuDefaults.itemColors(
                textColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        )
    }
}

private fun startGame(context: Context, game: Game) {
    Intent(
        context,
        EmulationActivity::class.java
    ).apply {
        putExtra(EmulationActivity.EXTRA_LAUNCH_PATH, game.path)
        context.startActivity(this)
    }
}

private fun shareLogFile(context: Context) {
    val logFileName = "log.txt"
    val logFile = File(NativeActiveSettings.getUserDataPath()).resolve(logFileName)

    if (!logFile.isFile) {
        Toast.makeText(context, tr("Log file doesn't exist"), Toast.LENGTH_LONG).show()
        return
    }

    val fileUri = DocumentsContract.buildDocumentUri(
        DocumentsProvider.AUTHORITY,
        DocumentsProvider.ROOT_ID + "/$logFileName"
    )

    val documentFile = DocumentFile.fromSingleUri(context, fileUri) ?: return

    val intent = Intent(Intent.ACTION_SEND)
        .setDataAndType(documentFile.uri, "text/plain")
        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        .putExtra(Intent.EXTRA_STREAM, documentFile.uri)

    context.startActivity(Intent.createChooser(intent, null))
}

private fun openCemuFolder(context: Context) {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
            .addCategory(Intent.CATEGORY_DEFAULT)
            .addFlags(
                Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                        or Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        intent.data = DocumentsContract.buildRootUri(
            DocumentsProvider.AUTHORITY,
            DocumentsProvider.ROOT_ID
        )
        context.startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(context, tr("Could not open Cemu folder"), Toast.LENGTH_LONG).show()
    }
}


private fun createShortcutForGame(
    context: Context,
    game: Game,
) {
    fun onFailedToCreateShortcut() {
        Toast.makeText(context, tr("Couldn't create shortcut for game"), Toast.LENGTH_LONG).show()
    }

    try {
        val shortcutManager = context.getSystemService(
            ShortcutManager::class.java
        )
        if (!shortcutManager.isRequestPinShortcutSupported) {
            onFailedToCreateShortcut()
            return
        }

        val icon = game.icon?.asAndroidBitmap().let {
            if (it != null) AndroidIcon.createWithBitmap(it)
            else AndroidIcon.createWithResource(context, R.mipmap.ic_launcher)
        }

        val intent = Intent(
            context,
            EmulationActivity::class.java
        )
        intent.action = Intent.ACTION_VIEW
        intent.putExtra(EmulationActivity.EXTRA_LAUNCH_PATH, game.path)

        val pinShortcutInfo = ShortcutInfo.Builder(context, game.titleId.toString())
            .setShortLabel(game.name!!)
            .setIntent(intent)
            .setIcon(icon)
            .build()

        val pinnedShortcutCallbackIntent =
            shortcutManager.createShortcutResultIntent(pinShortcutInfo)

        val successCallback = PendingIntent.getBroadcast(
            context,
            0,
            pinnedShortcutCallbackIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        shortcutManager.requestPinShortcut(pinShortcutInfo, successCallback.intentSender)
    } catch (_: Exception) {
        onFailedToCreateShortcut()
    }
}
