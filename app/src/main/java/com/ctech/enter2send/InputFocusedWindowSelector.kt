package com.ctech.enter2send

internal object InputFocusedWindowSelector {
    fun <T : Any> select(
        windows: Iterable<T>,
        isInputFocused: (T) -> Boolean
    ): T? {
        var focusedWindow: T? = null
        for (window in windows) {
            if (!isInputFocused(window)) continue
            if (focusedWindow != null) return null
            focusedWindow = window
        }
        return focusedWindow
    }
}
