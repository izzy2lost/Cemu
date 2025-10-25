package com.izzy2lost.weeu.gamelist.cover

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

class CoverDownloadService(private val context: Context) {
    private val client = OkHttpClient()
    private val coverCacheDir = File(context.cacheDir, "covers").apply { mkdirs() }
    
    init {
        GameTitleMapper.initialize(context)
    }

    suspend fun downloadCover(titleIdHex: String, region: String = "US"): File? = withContext(Dispatchers.IO) {
        try {
            val coverFile = getCoverFile(titleIdHex)
            if (coverFile.exists()) {
                println("Cover already exists: ${coverFile.absolutePath}")
                return@withContext coverFile
            }

            // Get game ID from mapper (this is the full 6-character GameTDB ID like ALZP01)
            val gameId = GameTitleMapper.getGameCodeFromTitleId(titleIdHex)
            println("Title ID: $titleIdHex -> Game ID: $gameId")
            
            if (gameId != null) {
                // If the game ID is already 6 characters, use it directly
                // Otherwise, append region code
                val fullGameId = if (gameId.length >= 6) {
                    gameId
                } else {
                    // GameTDB format: 4-char code + 2-char region (e.g., ALZP01 for US, ALZP02 for EU)
                    val regionCode = when(region) {
                        "US", "USA" -> "01"
                        "EU", "EUR" -> "02"
                        "JP", "JPN" -> "00"
                        else -> "01"
                    }
                    gameId + regionCode
                }
                
                println("Full game ID: $fullGameId")
                
                // Try multiple URL formats, prioritizing custom upscaled 3D covers from GitHub
                val urls = listOf(
                    // Custom upscaled 3D covers from GitHub (preferred - 4x quality)
                    "https://raw.githubusercontent.com/izzy2lost/WeeU-Covers/main/3D/$fullGameId.png",
                    // GameTDB 3D covers (fallback)
                    "https://art.gametdb.com/wiiu/cover3D/US/$fullGameId.png",
                    "https://art.gametdb.com/wiiu/cover3D/EN/$fullGameId.png",
                    "https://art.gametdb.com/wiiu/cover3D/EU/$fullGameId.png",
                    "https://art.gametdb.com/wiiu/cover3D/JA/$fullGameId.png",
                    // Full HQ covers (fallback)
                    "https://art.gametdb.com/wiiu/coverfullHQ/US/$fullGameId.png",
                    "https://art.gametdb.com/wiiu/coverfullHQ/EN/$fullGameId.png",
                    "https://art.gametdb.com/wiiu/coverfullHQ/EU/$fullGameId.png",
                    // Regular covers (last resort)
                    "https://art.gametdb.com/wiiu/cover/US/$fullGameId.png",
                    "https://art.gametdb.com/wiiu/cover/EN/$fullGameId.png",
                    "https://art.gametdb.com/wiiu/cover/EU/$fullGameId.png"
                )

                for (url in urls) {
                    try {
                        println("Trying URL: $url")
                        val request = Request.Builder()
                            .url(url)
                            .addHeader("User-Agent", "WeeU-Android/1.0")
                            .build()
                        client.newCall(request).execute().use { response ->
                            println("Response code: ${response.code}")
                            if (response.isSuccessful) {
                                response.body?.byteStream()?.use { input ->
                                    coverFile.outputStream().use { output ->
                                        input.copyTo(output)
                                    }
                                    println("Successfully downloaded cover from: $url")
                                    println("Saved to: ${coverFile.absolutePath}")
                                    return@withContext coverFile
                                }
                            }
                        }
                    } catch (e: Exception) {
                        println("Error downloading from $url: ${e.message}")
                        // Try next URL
                        continue
                    }
                }
                
                println("Failed to download cover from any URL for game ID: $fullGameId")
            } else {
                println("Could not determine game ID for title: $titleIdHex")
            }
            
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getCoverFile(titleId: String): File {
        return File(coverCacheDir, "$titleId.png")
    }

    fun hasCover(titleId: String): Boolean {
        return getCoverFile(titleId).exists()
    }
}
