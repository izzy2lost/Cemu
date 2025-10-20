package com.izzy2lost.weeu.common.android.context

import android.content.Context
import java.io.File

fun Context.internalFolder(): File {
    val externalFilesDir = getExternalFilesDir(null)
    if (externalFilesDir != null) {
        return externalFilesDir
    }
    return filesDir
}