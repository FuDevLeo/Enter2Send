package com.ctech.enter2send

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class SupportedAppProfilesTest {
    @Test
    fun resolvesSupportedPackages() {
        assertSame(
            SupportedAppProfiles.chatGpt,
            SupportedAppProfiles.forPackage("com.openai.chatgpt")
        )
        assertSame(
            SupportedAppProfiles.messenger,
            SupportedAppProfiles.forPackage("com.facebook.orca")
        )
        assertNull(SupportedAppProfiles.forPackage("com.example.other"))
        assertNull(SupportedAppProfiles.forPackage(null))
    }

    @Test
    fun keepsMessengerOptInWithoutChangingChatGptDefault() {
        assertTrue(SupportedAppProfiles.chatGpt.enabledByDefault)
        assertFalse(SupportedAppProfiles.messenger.enabledByDefault)
    }

    @Test
    fun matchesOnlyExactSemanticSendDescriptions() {
        val profile = SupportedAppProfiles.messenger

        assertTrue(profile.hasSendIdentity("Send", null))
        assertTrue(profile.hasSendIdentity(" send message ", null))
        assertFalse(profile.hasSendIdentity("Send Like", null))
        assertFalse(profile.hasSendIdentity("Resend", null))
    }

    @Test
    fun matchesOnlyKnownSendViewIdSuffixes() {
        val profile = SupportedAppProfiles.messenger

        assertTrue(profile.hasSendIdentity(null, "com.facebook.orca:id/send_button"))
        assertTrue(profile.hasSendIdentity(null, "COM.FACEBOOK.ORCA:ID/SEND_MESSAGE"))
        assertFalse(profile.hasSendIdentity(null, "com.facebook.orca:id/send_like_button"))
        assertFalse(profile.hasSendIdentity(null, "com.facebook.orca:id/search"))
    }

    @Test
    fun containsExactlyTheDeclaredSupportedApps() {
        assertEquals(
            setOf("com.openai.chatgpt", "com.facebook.orca"),
            SupportedAppProfiles.all.map { it.packageName }.toSet()
        )
    }
}
