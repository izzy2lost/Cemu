package com.izzy2lost.weeu.gamelist

import android.content.Context
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStreamReader

data class GameInfo(
    val id: String,
    val title: String?,
    val synopsis: String?,
    val developer: String?,
    val publisher: String?,
    val releaseDate: String?,
    val genre: String?,
    val rating: String?,
    val ratingDescriptors: List<String>,
    val players: String?,
    val wifiPlayers: String?,
    val wifiFeatures: List<String>
)

object GameInfoLoader {
    private var gameInfoCache: MutableMap<String, GameInfo> = mutableMapOf()
    
    fun loadGameInfo(context: Context, gameId: String): GameInfo? {
        // Check cache first
        if (gameInfoCache.containsKey(gameId)) {
            return gameInfoCache[gameId]
        }
        
        try {
            val inputStream = context.assets.open("wiiutdb.xml")
            val reader = InputStreamReader(inputStream)
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(reader)
            
            var eventType = parser.eventType
            var currentGameId: String? = null
            var title: String? = null
            var synopsis: String? = null
            var developer: String? = null
            var publisher: String? = null
            var releaseDate: String? = null
            var genre: String? = null
            var rating: String? = null
            val ratingDescriptors = mutableListOf<String>()
            var players: String? = null
            var wifiPlayers: String? = null
            val wifiFeatures = mutableListOf<String>()
            var inGame = false
            var inLocale = false
            var inRating = false
            var inWifi = false
            var inInput = false
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "game" -> {
                                inGame = true
                                // Reset values for new game
                                currentGameId = null
                                title = null
                                synopsis = null
                                developer = null
                                publisher = null
                                releaseDate = null
                                genre = null
                                rating = null
                                ratingDescriptors.clear()
                                players = null
                                wifiPlayers = null
                                wifiFeatures.clear()
                            }
                            "id" -> {
                                if (inGame) {
                                    if (parser.next() == XmlPullParser.TEXT) {
                                        currentGameId = parser.text
                                    }
                                }
                            }
                            "locale" -> {
                                if (inGame && parser.getAttributeValue(null, "lang") == "EN") {
                                    inLocale = true
                                }
                            }
                            "title" -> {
                                if (inLocale) {
                                    if (parser.next() == XmlPullParser.TEXT) {
                                        title = parser.text
                                    }
                                }
                            }
                            "synopsis" -> {
                                if (inLocale) {
                                    if (parser.next() == XmlPullParser.TEXT) {
                                        synopsis = parser.text
                                    }
                                }
                            }
                            "developer" -> {
                                if (inGame) {
                                    if (parser.next() == XmlPullParser.TEXT) {
                                        developer = parser.text
                                    }
                                }
                            }
                            "publisher" -> {
                                if (inGame) {
                                    if (parser.next() == XmlPullParser.TEXT) {
                                        publisher = parser.text
                                    }
                                }
                            }
                            "date" -> {
                                if (inGame) {
                                    val year = parser.getAttributeValue(null, "year")
                                    val month = parser.getAttributeValue(null, "month")
                                    val day = parser.getAttributeValue(null, "day")
                                    releaseDate = "$year-$month-$day"
                                }
                            }
                            "genre" -> {
                                if (inGame) {
                                    if (parser.next() == XmlPullParser.TEXT) {
                                        genre = parser.text
                                    }
                                }
                            }
                            "rating" -> {
                                if (inGame) {
                                    inRating = true
                                    val type = parser.getAttributeValue(null, "type")
                                    val value = parser.getAttributeValue(null, "value")
                                    rating = "$type: $value"
                                }
                            }
                            "descriptor" -> {
                                if (inRating) {
                                    if (parser.next() == XmlPullParser.TEXT) {
                                        parser.text?.let { ratingDescriptors.add(it) }
                                    }
                                }
                            }
                            "wi-fi" -> {
                                if (inGame) {
                                    inWifi = true
                                    wifiPlayers = parser.getAttributeValue(null, "players")
                                }
                            }
                            "feature" -> {
                                if (inWifi) {
                                    if (parser.next() == XmlPullParser.TEXT) {
                                        parser.text?.let { wifiFeatures.add(it) }
                                    }
                                }
                            }
                            "input" -> {
                                if (inGame) {
                                    inInput = true
                                    players = parser.getAttributeValue(null, "players")
                                }
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        when (parser.name) {
                            "game" -> {
                                // Check if this is the game we're looking for
                                if (currentGameId == gameId) {
                                    val gameInfo = GameInfo(
                                        id = currentGameId,
                                        title = title,
                                        synopsis = synopsis,
                                        developer = developer,
                                        publisher = publisher,
                                        releaseDate = releaseDate,
                                        genre = genre,
                                        rating = rating,
                                        ratingDescriptors = ratingDescriptors.toList(),
                                        players = players,
                                        wifiPlayers = wifiPlayers,
                                        wifiFeatures = wifiFeatures.toList()
                                    )
                                    gameInfoCache[gameId] = gameInfo
                                    reader.close()
                                    return gameInfo
                                }
                                inGame = false
                            }
                            "locale" -> inLocale = false
                            "rating" -> inRating = false
                            "wi-fi" -> inWifi = false
                            "input" -> inInput = false
                        }
                    }
                }
                eventType = parser.next()
            }
            
            reader.close()
        } catch (e: Exception) {
            println("Error loading game info for $gameId: ${e.message}")
            e.printStackTrace()
        }
        
        return null
    }
    
    fun clearCache() {
        gameInfoCache.clear()
    }
}
