package com.izzy2lost.weeu

import android.app.Application
import com.izzy2lost.weeu.common.android.context.internalFolder
import com.izzy2lost.weeu.common.ui.localization.setLanguage
import com.izzy2lost.weeu.common.ui.localization.setTranslations
import com.izzy2lost.weeu.nativeinterface.NativeActiveSettings.initializeActiveSettings
import com.izzy2lost.weeu.nativeinterface.NativeActiveSettings.setInternalDir
import com.izzy2lost.weeu.nativeinterface.NativeActiveSettings.setNativeLibDir
import com.izzy2lost.weeu.nativeinterface.NativeEmulation.initializeEmulation
import com.izzy2lost.weeu.nativeinterface.NativeEmulation.setDPI
import com.izzy2lost.weeu.nativeinterface.NativeGraphicPacks.refreshGraphicPacks
import com.izzy2lost.weeu.nativeinterface.NativeLogging.crashLog
import com.izzy2lost.weeu.nativeinterface.NativeSwkbd.initializeSwkbd
import com.izzy2lost.weeu.common.settings.SettingsManager
import com.izzy2lost.weeu.nativeinterface.NativeFiles
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.util.regex.Pattern

class CemuApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        configureExceptionHandler()

        SettingsManager.initialize(this)

        NativeFiles.initialize(contentResolver)

        initializeTranslations()

        initializeCemu()

        saveDataFiles()
    }

    private fun initializeTranslations() {
        setTranslations(this)
        setLanguage(SettingsManager.guiSettings.language, this)
    }

    private fun saveDataFiles() {
        val dataFolder = File(internalCemuDataFolder)

        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            return
        }

        val hashFileName = "hash.txt"
        val hashFile = dataFolder.resolve(hashFileName)
        val oldHash = if (hashFile.isFile) hashFile.readText() else "invalid"

        val newHash = try {
            assets.open(hashFileName).use { it.reader().readText() }
        } catch (_: IOException) {
            return
        }

        if (oldHash == newHash) {
            return
        }

        dataFolder.deleteRecursively()
        dataFolder.mkdirs()
        dataFolder.resolve(hashFileName).writeText(newHash)

        fun traverseAssets(path: String = ""): Iterator<String> = iterator {
            val assetFiles = assets.list(path) ?: return@iterator

            if (assetFiles.isEmpty()) {
                yield(path)
            }

            for (assetFile in assetFiles) {
                val assetPath = path + (if (path == "") "" else "/") + assetFile
                for (file in traverseAssets(assetPath)) {
                    yield(file)
                }
            }
        }

        val filePatterns = arrayOf(
            Pattern.compile("gameProfiles/.*"),
            Pattern.compile("resources/.*"),
        )

        fun isFileValid(file: String): Boolean {
            return filePatterns.any { pattern -> pattern.matcher(file).matches() }
        }

        for (assetFile in traverseAssets()) {
            if (!isFileValid(assetFile)) {
                continue
            }

            val outFile = dataFolder.resolve(assetFile)
            outFile.parentFile?.mkdirs()
            assets.open(assetFile)
                .use { asset -> outFile.outputStream().use { out -> asset.copyTo(out) } }
        }
    }

    private fun configureExceptionHandler() {
        if (DefaultUncaughtExceptionHandler == null) {
            DefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        }
        Thread.setDefaultUncaughtExceptionHandler { thread: Thread, exception: Throwable ->
            val stringWriter = StringWriter()
            val printWriter = PrintWriter(stringWriter)
            exception.printStackTrace(printWriter)
            val stacktrace = stringWriter.toString()
            crashLog(stacktrace)
            DefaultUncaughtExceptionHandler!!.uncaughtException(
                thread,
                exception
            )
        }
    }

    private fun initializeCemu() {
        val displayMetrics = resources.displayMetrics
        setDPI(displayMetrics.density)
        initializeActiveSettings(
            userDataPath = internalCemuUserFolder,
            dataPath = internalCemuDataFolder,
            cachePath = internalCemuUserFolder,
        )
        setNativeLibDir(applicationInfo.nativeLibraryDir)
        setInternalDir(dataDir.absolutePath)
        initializeEmulation()
        initializeSwkbd()
        refreshGraphicPacks()
    }

    private val internalCemuDataFolder: String
        get() = internalFolder().resolve("data").toString()

    private val internalCemuUserFolder: String
        get() = internalFolder().toString()

    companion object {
        init {
            System.loadLibrary("CemuAndroid")
        }

        private var DefaultUncaughtExceptionHandler: Thread.UncaughtExceptionHandler? = null
    }
}
