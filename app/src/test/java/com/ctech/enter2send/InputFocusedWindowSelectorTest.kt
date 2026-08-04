package com.ctech.enter2send

import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Test

class InputFocusedWindowSelectorTest {
    @Test
    fun selectsTheOnlyInputFocusedWindow() {
        val staleSupportedWindow = Window("com.openai.chatgpt", isFocused = false)
        val currentWindow = Window("com.example.notes", isFocused = true)

        val selected = InputFocusedWindowSelector.select(
            listOf(staleSupportedWindow, currentWindow),
            Window::isFocused
        )

        assertSame(currentWindow, selected)
        assertNull(SupportedAppProfiles.forPackage(selected?.packageName))
    }

    @Test
    fun ignoresVisibleSupportedWindowsThatDoNotOwnInputFocus() {
        val staleChatGptWindow = Window("com.openai.chatgpt", isFocused = false)
        val staleMessengerWindow = Window("com.facebook.orca", isFocused = false)

        val selected = InputFocusedWindowSelector.select(
            listOf(staleChatGptWindow, staleMessengerWindow),
            Window::isFocused
        )

        assertNull(selected)
    }

    @Test
    fun selectsTheCurrentSupportedWindowInsteadOfAnotherSupportedWindow() {
        val staleChatGptWindow = Window("com.openai.chatgpt", isFocused = false)
        val focusedMessengerWindow = Window("com.facebook.orca", isFocused = true)

        val selected = InputFocusedWindowSelector.select(
            listOf(staleChatGptWindow, focusedMessengerWindow),
            Window::isFocused
        )

        assertSame(focusedMessengerWindow, selected)
        assertSame(
            SupportedAppProfiles.messenger,
            SupportedAppProfiles.forPackage(selected?.packageName)
        )
    }

    @Test
    fun failsOpenWhenInputFocusIsAmbiguous() {
        val firstWindow = Window("com.openai.chatgpt", isFocused = true)
        val secondWindow = Window("com.facebook.orca", isFocused = true)

        val selected = InputFocusedWindowSelector.select(
            listOf(firstWindow, secondWindow),
            Window::isFocused
        )

        assertNull(selected)
    }

    private data class Window(
        val packageName: String,
        val isFocused: Boolean
    )
}
