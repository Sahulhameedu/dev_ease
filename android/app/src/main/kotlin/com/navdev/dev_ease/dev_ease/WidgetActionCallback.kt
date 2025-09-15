package com.navdev.dev_ease.dev_ease

import android.content.Context
import android.content.Intent
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.updateAll
import android.provider.Settings
import android.util.Log
import es.antonborri.home_widget.HomeWidgetPlugin

class WidgetActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val action = parameters[ActionParameters.Key<String>("action")]
        
        // Create intent to launch the main app with data
        // val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        // intent?.apply {
            when (action) {
                // ControlWidget.ACTION_TOGGLE_LIGHT -> {
                //     data = android.net.Uri.parse("homeWidget://toggleLight")
                // }
                // ControlWidget.ACTION_START_TIMER -> {
                //     val duration = parameters[ActionParameters.Key<String>("duration")] ?: "300"
                //     data = android.net.Uri.parse("homeWidget://startTimer?duration=$duration")
                // }
                // ControlWidget.ACTION_REFRESH_DATA -> {
                //     data = android.net.Uri.parse("homeWidget://refreshData")
                // }
                // ControlWidget.ACTION_OPEN_APP -> {
                //     data = android.net.Uri.parse("homeWidget://openApp")
                // }
                ControlWidget.TOGGLE_DEVELOPER_OPTIONS -> {
                    Log.d("WidgetActionCallback", "Toggling developer options")
                    // data = android.net.Uri.parse("homeWidget://toggleDevOptions")
                    toggleDeveloperOptions(context)
                }
                ControlWidget.ACTION_TOGGLE_USB_DEBUGGING -> {
                    Log.d("WidgetActionCallback", "Toggling USB debugging")
                    toggleUsbDebugging(context)
                    // data = android.net.Uri.parse("homeWidget://toggleUsbDebugging")
                }
                ControlWidget.ACTION_REFRESH_DATA -> {
                    Log.d("WidgetActionCallback", "Toggling USB debugging")
                         // Always update the widget after an action
                    ControlWidget().refreshAllWidgets(context)
                    // data = android.net.Uri.parse("homeWidget://toggleUsbDebugging")
                }
            }
            ControlWidget().refreshAllWidgets(context)
            ControlWidget().updateAll(context)
            // context.startActivity(this)
        // }
    }

    private suspend fun toggleDeveloperOptions(context: Context) {
        try {
            val currentStatus = Settings.Global.getInt(
                context.contentResolver,
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
                0
            ) == 1
            
            // Toggle the setting
            Settings.Global.putInt(
                context.contentResolver,
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
                if (currentStatus) 0 else 1
            )
            
            // Update Flutter widget data
            // HomeWidgetPlugin.saveWidgetData(context, "dev_options", !currentStatus)
            // HomeWidgetPlugin.updateWidget(context)
            
        } catch (e: SecurityException) {
            // Handle permission error - you might want to show a notification
            // or open settings page
            openDeveloperOptionsSettings(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun toggleUsbDebugging(context: Context) {
        try {
            val currentStatus = Settings.Global.getInt(
                context.contentResolver,
                Settings.Global.ADB_ENABLED,
                0
            ) == 1
            
            // Toggle the setting
            Settings.Global.putInt(
                context.contentResolver,
                Settings.Global.ADB_ENABLED,
                if (currentStatus) 0 else 1
            )
            
            // Update Flutter widget data
            // HomeWidgetPlugin.saveWidgetData(context, "toggle_usb_debugging", !currentStatus)
            // HomeWidgetPlugin.updateWidget(context)
            
        } catch (e: SecurityException) {
            // Handle permission error
            openDeveloperOptionsSettings(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun toggleStayAwake(context: Context) {
        try {
            val resolver = context.contentResolver

            // Read current value (0 = off, >0 = on)
            val current = Settings.Global.getInt(
                resolver,
                Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                0
            )

            val newValue = if (current == 0) {
                // Turn ON: keep awake for USB + AC
                3
            } else {
                // Turn OFF
                0
            }

            val success = Settings.Global.putInt(
                resolver,
                Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                newValue
            )

            Log.d("StayAwakeToggle", "Set stay awake to $newValue (success=$success)")

            // If you want to update a widget or send result back:
            // HomeWidgetPlugin.saveWidgetData(context, "stay_awake", newValue != 0)
            // HomeWidgetPlugin.updateWidget(context)

        } catch (se: SecurityException) {
            // Permission missing — maybe open Developer Options so user can enable it
            openDeveloperOptionsSettings(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openDeveloperOptionsSettings(context: Context) {
        try {
            val intent = android.content.Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to general settings
            val intent = android.content.Intent(Settings.ACTION_SETTINGS)
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
    
}
