package com.izzy2lost.weeu.gamelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.izzy2lost.weeu.common.ui.components.ScreenContent
import com.izzy2lost.weeu.common.ui.localization.regionToString
import com.izzy2lost.weeu.common.ui.localization.tr
import com.izzy2lost.weeu.gamelist.cover.GameTitleMapper
import com.izzy2lost.weeu.nativeinterface.NativeGameTitles.Game
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun GameDetailsScreen(game: Game?, navigateBack: () -> Unit) {
    if (game == null)
        return

    val context = LocalContext.current
    var gameInfo by remember { mutableStateOf<GameInfo?>(null) }
    
    LaunchedEffect(game.titleId) {
        GameTitleMapper.initialize(context)
        val titleIdHex = game.titleId.toString(16).uppercase().padStart(16, '0')
        val gameId = GameTitleMapper.getGameCodeFromTitleId(titleIdHex)
        if (gameId != null) {
            gameInfo = GameInfoLoader.loadGameInfo(context, gameId)
        }
    }

    ScreenContent(
        appBarText = tr("About title"),
        contentModifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        contentVerticalArrangement = Arrangement.spacedBy(16.dp),
        navigateBack = navigateBack,
    ) {
        GameDetails(game, gameInfo)
    }
}

@Composable
fun GameDetails(game: Game, gameInfo: GameInfo?) {
    GameIcon(
        game = game,
        modifier = Modifier.size(128.dp),
    )
    
    // Game database information
    if (gameInfo != null) {
        gameInfo.title?.let {
            TitleDetailsEntry(entryName = tr("Title"), entryData = it)
        }
        gameInfo.developer?.let {
            TitleDetailsEntry(entryName = tr("Developer"), entryData = it)
        }
        gameInfo.publisher?.let {
            TitleDetailsEntry(entryName = tr("Publisher"), entryData = it)
        }
        gameInfo.releaseDate?.let {
            TitleDetailsEntry(entryName = tr("Release Date"), entryData = it)
        }
        gameInfo.genre?.let {
            TitleDetailsEntry(entryName = tr("Genre"), entryData = it)
        }
        gameInfo.rating?.let {
            TitleDetailsEntry(entryName = tr("Rating"), entryData = it)
        }
        if (gameInfo.ratingDescriptors.isNotEmpty()) {
            TitleDetailsEntry(
                entryName = tr("Content Descriptors"),
                entryData = gameInfo.ratingDescriptors.joinToString(", ")
            )
        }
        gameInfo.players?.let {
            TitleDetailsEntry(entryName = tr("Players"), entryData = it)
        }
        gameInfo.wifiPlayers?.let {
            TitleDetailsEntry(entryName = tr("Wi-Fi Players"), entryData = it)
        }
        if (gameInfo.wifiFeatures.isNotEmpty()) {
            TitleDetailsEntry(
                entryName = tr("Wi-Fi Features"),
                entryData = gameInfo.wifiFeatures.joinToString(", ")
            )
        }
        gameInfo.synopsis?.let {
            TitleDetailsEntry(entryName = tr("Synopsis"), entryData = it)
        }
    }
    
    // Local game information
    TitleDetailsEntry(entryName = tr("Title ID"), entryData = game.titleId)
    TitleDetailsEntry(entryName = tr("Version"), entryData = game.version)
    TitleDetailsEntry(entryName = tr("DLC"), entryData = game.dlc)
    TitleDetailsEntry(
        entryName = tr("You've played"),
        entryData = getTimePlayed(game)
    )
    TitleDetailsEntry(
        entryName = tr("Last played"),
        entryData = getLastPlayedDate(game)
    )
    TitleDetailsEntry(
        entryName = tr("Region"),
        entryData = regionToString(game.region)
    )
    TitleDetailsEntry(
        entryName = tr("Path"),
        entryData = game.path
    )
}


private fun getTimePlayed(game: Game): String {
    if (game.minutesPlayed == 0) {
        return tr("Never played")
    }
    if (game.minutesPlayed < 60) {
        return tr("Minutes: {0}", game.minutesPlayed)
    }
    return tr(
        "Hours: {0} Minutes: {1}",
        game.minutesPlayed / 60,
        game.minutesPlayed % 60
    )
}

private val DateFormatter = DateTimeFormatter.ofLocalizedDate(
    FormatStyle.SHORT
)

private fun getLastPlayedDate(game: Game): String {
    if (game.lastPlayedYear.toInt() == 0) {
        return tr("Never played")
    }
    val lastPlayedDate = LocalDate.of(
        game.lastPlayedYear.toInt(),
        game.lastPlayedMonth.toInt(),
        game.lastPlayedDay.toInt()
    )
    return DateFormatter.format(lastPlayedDate)
}

@Composable
private fun <T> TitleDetailsEntry(entryName: String, entryData: T?) {
    Column {
        Text(
            text = entryName,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
        )
        Text(
            text = entryData?.toString() ?: "",
            fontSize = 16.sp,
        )
    }
}
