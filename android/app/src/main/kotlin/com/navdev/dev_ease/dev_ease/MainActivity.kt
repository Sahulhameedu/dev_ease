package com.navdev.dev_ease

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel


class MainActivity : FlutterActivity(){
    private val CHANNEL = "settingsChannel"
    private lateinit var methodChannel: MethodChannel
    private val WIRELESS_ADB_ENABLED = "adb_wifi_enabled"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine){
        super.configureFlutterEngine(flutterEngine)

        methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
        methodChannel.setMethodCallHandler { call, result ->
            when (call.method) {
                "getSettings" -> {
                    result.success(getSettingsStatus().toMap())
                }
                "updateSettings" -> {

                    if(hasWriteSecureSettings()){
                        val argsMap = call.arguments as? Map<*, *>
                        val stringBooleanMap = argsMap?.mapNotNull { (key, value) ->
                            if (key is String && value is Boolean) {
                                key to value
                            } else null
                        }?.toMap() ?: emptyMap()

                        updateSettings(DeveloperOptions.fromMap(stringBooleanMap))
                        result.success(getSettingsStatus().toMap())
                    }else{
                        result.error("", "Permission not allowed", "Allow permissions to continue")
                    }
                }
                else -> {
                    result.notImplemented()
                }
            }
        }
    }

    private fun hasWriteSecureSettings(): Boolean {
        return packageManager.checkPermission(
            Manifest.permission.WRITE_SECURE_SETTINGS,
            packageName
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getSettingsStatus():DeveloperOptions{
        val devOptionsEnabled = Settings.Global.getInt(
            contentResolver,
            Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
            0
        )
        val adbEnabled = Settings.Global.getInt(
            contentResolver,
            Settings.Global.ADB_ENABLED,
            0
        )
        val wifiAdbEnabled = Settings.Global.getInt(
            contentResolver,
            WIRELESS_ADB_ENABLED,
            0
        )

        val settings = DeveloperOptions(devOptionsEnabled == 1, adbEnabled == 1, wifiAdbEnabled == 1)
        return settings
    }

    private fun updateSettings(settings: DeveloperOptions) {
        Settings.Global.putInt(
            contentResolver,
            Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
            if (settings.devModeEnabled) 1 else 0
        )
        Settings.Global.putInt(
            contentResolver,
            Settings.Global.ADB_ENABLED,
            if (settings.adbEnabled) 1 else 0
        )
        Settings.Global.putInt(
            contentResolver,
            WIRELESS_ADB_ENABLED,
            if (settings.wifiAdbEnabled) 1 else 0
        )
    }
}
