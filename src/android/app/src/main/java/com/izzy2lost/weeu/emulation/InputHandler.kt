package com.izzy2lost.weeu.emulation

import android.view.KeyEvent
import android.view.MotionEvent
import com.izzy2lost.weeu.common.android.inputdevice.isGameController
import com.izzy2lost.weeu.common.android.motionevent.isMotionEventFromJoystickOrGamepad
import com.izzy2lost.weeu.nativeinterface.NativeInput.onNativeAxis
import com.izzy2lost.weeu.nativeinterface.NativeInput.onNativeKey

private fun KeyEvent.isSpecialKey(): Boolean {
    return keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
            || keyCode == KeyEvent.KEYCODE_VOLUME_UP
            || keyCode == KeyEvent.KEYCODE_CAMERA
            || keyCode == KeyEvent.KEYCODE_ZOOM_IN
            || keyCode == KeyEvent.KEYCODE_ZOOM_OUT
}

object InputHandler {
    fun onKeyEvent(event: KeyEvent): Boolean {
        if (event.isSpecialKey()) {
            return false
        }
        if (event.deviceId < 0) {
            return false
        }
        val device = event.device
        if (!device.isGameController()) {
            return false
        }
        onNativeKey(
            device.descriptor,
            device.name,
            event.keyCode,
            event.action == KeyEvent.ACTION_DOWN
        )
        return true
    }

    fun onMotionEvent(event: MotionEvent): Boolean {
        if (!event.isMotionEventFromJoystickOrGamepad()) {
            return false
        }
        val device = event.device
        val actionPointerIndex = event.actionIndex
        for (motionRange in device.motionRanges) {
            val axisValue = event.getAxisValue(motionRange.axis, actionPointerIndex)
            val axis = motionRange.axis
            onNativeAxis(device.descriptor, device.name, axis, axisValue)
        }
        return true
    }
}