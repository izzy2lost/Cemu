package com.izzy2lost.weeu.nativeinterface

object NativeSwkbd {
    @JvmStatic
    external fun initializeSwkbd()

    @JvmStatic
    external fun setCurrentInputText(text: String?)

    @JvmStatic
    external fun onTextChanged(text: String?)

    @JvmStatic
    external fun onFinishedInputEdit()
}
