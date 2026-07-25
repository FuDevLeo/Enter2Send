package com.ctech.enter2send

import java.util.Locale

data class SupportedAppProfile(
    val displayName: String,
    val packageName: String,
    internal val preferenceKey: String,
    internal val enabledByDefault: Boolean,
    private val sendDescriptions: Set<String>,
    private val sendViewIdSuffixes: Set<String>
) {
    fun hasSendIdentity(
        contentDescription: CharSequence?,
        viewIdResourceName: String?
    ): Boolean {
        val description = contentDescription?.toString()?.trim()
        if (description != null && sendDescriptions.any {
                it.equals(description, ignoreCase = true)
            }
        ) {
            return true
        }

        val viewId = viewIdResourceName?.lowercase(Locale.ROOT) ?: return false
        return sendViewIdSuffixes.any(viewId::endsWith)
    }
}

object SupportedAppProfiles {
    val chatGpt = SupportedAppProfile(
        displayName = "ChatGPT",
        packageName = "com.openai.chatgpt",
        preferenceKey = "app_chatgpt_enabled",
        enabledByDefault = true,
        sendDescriptions = setOf("Send", "Send message"),
        sendViewIdSuffixes = setOf(
            "/send",
            "/send_button",
            "/send_message",
            "/send_message_button"
        )
    )

    val messenger = SupportedAppProfile(
        displayName = "Messenger",
        packageName = "com.facebook.orca",
        preferenceKey = "app_messenger_enabled",
        enabledByDefault = false,
        sendDescriptions = setOf("Send", "Send message"),
        sendViewIdSuffixes = setOf(
            "/send",
            "/send_button",
            "/send_message",
            "/send_message_button"
        )
    )

    val all: List<SupportedAppProfile> = listOf(chatGpt, messenger)

    fun forPackage(packageName: String?): SupportedAppProfile? =
        all.singleOrNull { it.packageName == packageName }
}
