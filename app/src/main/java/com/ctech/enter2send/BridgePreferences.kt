package com.ctech.enter2send

import android.content.Context

object BridgePreferences {
    private const val FILE_NAME = "bridge_preferences"
    private const val KEY_ENABLED = "master_enabled"

    fun isMasterEnabled(context: Context): Boolean =
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_ENABLED, true)

    fun setMasterEnabled(context: Context, enabled: Boolean) {
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_ENABLED, enabled)
            .apply()
    }

    fun isAppEnabled(context: Context, profile: SupportedAppProfile): Boolean =
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            .getBoolean(profile.preferenceKey, profile.enabledByDefault)

    fun setAppEnabled(
        context: Context,
        profile: SupportedAppProfile,
        enabled: Boolean
    ) {
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(profile.preferenceKey, enabled)
            .apply()
    }
}
