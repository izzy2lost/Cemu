package com.izzy2lost.weeu.gamelist.cover

import android.content.Context
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader

object GameTitleMapper {
    private var titleIdToGameIdMap: Map<String, String?>? = null
    private val json = Json { ignoreUnknownKeys = true }

    fun initialize(context: Context) {
        if (titleIdToGameIdMap != null) {
            println("GameTitleMapper already initialized")
            return
        }
        
        try {
            // Load title ID to game ID mapping
            println("Loading titleid_to_gameid_mapping.json from assets...")
            val mapInputStream = context.assets.open("titleid_to_gameid_mapping.json")
            val mapReader = BufferedReader(InputStreamReader(mapInputStream))
            val mapJsonString = mapReader.use { it.readText() }
            titleIdToGameIdMap = json.decodeFromString(mapJsonString)
            println("Loaded ${titleIdToGameIdMap?.size} title ID mappings")
        } catch (e: Exception) {
            println("Error loading game data: ${e.message}")
            e.printStackTrace()
            titleIdToGameIdMap = emptyMap()
        }
    }

    fun getGameCodeFromTitleId(titleIdHex: String): String? {
        val normalizedId = titleIdHex.lowercase().padStart(16, '0')
        println("Looking up title ID: $normalizedId")
        
        // First, try to get the game ID from the mapping file
        val gameId = titleIdToGameIdMap?.get(normalizedId)
        if (gameId != null) {
            println("Found game ID in mapping: $gameId")
            return gameId
        }
        
        // If not found in mapping, try to extract from title ID
        println("Game ID not found in mapping, attempting extraction")
        val extractedCode = extractGameCode(normalizedId)
        println("Extracted game code: $extractedCode")
        
        return extractedCode
    }

    private fun extractGameCode(titleIdHex: String): String? {
        if (titleIdHex.length != 16) return null
        
        // Wii U title ID format: 0005000010XXXXXX
        // GameTDB format: 6-char game ID (e.g., ALZP01)
        // The game code is encoded in bytes 10-13 (hex positions 8-15)
        
        // Extract last 8 hex digits: positions 8-15
        val gameIdHex = titleIdHex.substring(8, 16)
        
        // Convert hex to bytes and try to extract ASCII game code
        return try {
            // Take first 8 hex chars (4 bytes) for the game code
            val codeBytes = gameIdHex.substring(0, 8).chunked(2)
                .map { it.toInt(16).toByte() }
                .toByteArray()
            
            // Convert to ASCII string
            val gameCode = String(codeBytes, Charsets.US_ASCII)
                .filter { it.isLetterOrDigit() }
            
            // GameTDB format is typically 6 characters (4 game + 2 region)
            // For now, just return the 4-character game code
            // The region will be added by the download service
            if (gameCode.length >= 4) {
                gameCode.take(4).uppercase()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
