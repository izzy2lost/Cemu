package com.izzy2lost.weeu.emulation

import android.view.SurfaceHolder

abstract class SurfaceChangedListener : SurfaceHolder.Callback {
    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {

    }

    abstract fun surfaceChanged()

    override fun surfaceChanged(
        surfaceHolder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int
    ) {
        surfaceChanged()
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
    }
}