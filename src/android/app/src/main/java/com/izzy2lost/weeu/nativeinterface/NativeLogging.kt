package com.izzy2lost.weeu.nativeinterface

object NativeLogging {
    @JvmStatic
    external fun log(message: String?)

    @JvmStatic
    external fun crashLog(stacktrace: String?)
}
