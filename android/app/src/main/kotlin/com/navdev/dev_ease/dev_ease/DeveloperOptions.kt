package com.navdev.dev_ease

data class DeveloperOptions(
    val devModeEnabled: Boolean = false,
    val adbEnabled: Boolean = false,
    val wifiAdbEnabled: Boolean = false
) {
    fun toMap(): Map<String, Boolean> {
        return mapOf(
            "devModeEnabled" to devModeEnabled,
            "adbEnabled" to adbEnabled,
            "wifiAdbEnabled" to wifiAdbEnabled
        )
    }

    companion object {
        fun fromMap(map: Map<String, Boolean>): DeveloperOptions {
            return DeveloperOptions(
                devModeEnabled = map["devModeEnabled"] ?: false,
                adbEnabled = map["adbEnabled"] ?: false,
                wifiAdbEnabled = map["wifiAdbEnabled"] ?: false
            )
        }
    }
}