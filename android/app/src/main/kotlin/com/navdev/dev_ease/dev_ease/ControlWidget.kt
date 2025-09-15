package com.navdev.dev_ease.dev_ease


import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
// import androidx.glance.appwidget.state.PreferencesGlanceStateDefinition
 import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.currentState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.action.Action
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.compose.ui.graphics.Brush
// import es.antonborri.home_widget.HomeWidgetPlugin
import android.content.Intent
import android.provider.Settings
import androidx.compose.ui.geometry.Offset
import androidx.glance.GlanceId
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.state.GlanceStateDefinition
import androidx.datastore.preferences.core.booleanPreferencesKey



class ControlWidget : GlanceAppWidget() {

     override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    companion object {
        const val TOGGLE_DEVELOPER_OPTIONS = "toggle_dev_options"
        const val ACTION_TOGGLE_USB_DEBUGGING = "toggle_usb_debugging"
        const val ACTION_REFRESH_DATA = "refresh_data"
        val IS_ENABLED_KEY = booleanPreferencesKey("dev_options_enabled")
        val USB_DEBUGGING_KEY = booleanPreferencesKey("usb_debugging_enabled")
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        updateAppWidgetState(context, id) { prefs ->
            val isEnabled = isDeveloperOptionsEnabled(context)
            val isUsbDebugging = isUsbDebuggingEnabled(context)
            prefs[IS_ENABLED_KEY] = isEnabled
            prefs[USB_DEBUGGING_KEY] = isUsbDebugging
        }
        provideContent {
            WidgetContent(context)
        }
    }

    public fun isDeveloperOptionsEnabled(context: Context): Boolean {
        return try {
            Settings.Global.getInt(
                context.contentResolver,
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
                0
            ) == 1
        } catch (e: Exception) {
            false
        }
    }

    // Function to check if USB Debugging is enabled
    public fun isUsbDebuggingEnabled(context: Context): Boolean {
        return try {
            Settings.Global.getInt(
                context.contentResolver,
                Settings.Global.ADB_ENABLED,
                0
            ) == 1
        } catch (e: Exception) {
            false
        }
    }

    suspend fun refreshAllWidgets(context: Context) {
        updateAll(context)
    }
}

@Composable
private fun WidgetContent(context: Context) {
    // Get data from Flutter
    // val widgetData = HomeWidgetPlugin.getData(context)
    // val developerOptsStatus = widgetData.getBoolean("dev_options", false)
    // val toggleUsbDebugging = widgetData.getBoolean("toggle_usb_debugging", false)
    // val timerValue = widgetData.getString("timer_value", "00:00") ?: "00:00"
    // val lastUpdate = widgetData.getString("last_update", "Never") ?: "Never"

    val prefs = currentState<androidx.datastore.preferences.core.Preferences>()
    val devEnabled = prefs[ControlWidget.IS_ENABLED_KEY] ?: false
    val usbEnabled = prefs[ControlWidget.USB_DEBUGGING_KEY] ?: false

        
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(Color(0xFF667EEA)))
            .padding(12.dp),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "Developer Controls",
            style = TextStyle(
                color = ColorProvider(Color.White),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = GlanceModifier.fillMaxWidth(),
        )
        
        Spacer(modifier = GlanceModifier.height(12.dp))

        Column(
            modifier = GlanceModifier
                .fillMaxWidth()
                .background(ColorProvider(Color.White))
                .cornerRadius(24.dp)
                .padding(16.dp)
        ) {
            SettingRow(
                label = "Developer Options",
                description = "Enable Android Developer Options",
                value = devEnabled,
                action = actionRunCallback<WidgetActionCallback>(
                 parameters = actionParametersOf(  ActionParameters.Key<String>("action") to ControlWidget.TOGGLE_DEVELOPER_OPTIONS )
                ),
                activeColor = Color(0xFF667EEA)
            )

            Spacer(GlanceModifier.height(12.dp))

            SettingRow(
                label = "USB Debugging",
                description = "Debug over USB",
                value = usbEnabled,
                action = actionRunCallback<WidgetActionCallback>(
                    parameters = actionParametersOf(
                        ActionParameters.Key<String>("action") to ControlWidget.ACTION_TOGGLE_USB_DEBUGGING
                    )
                ),
                activeColor = Color(0xFFF5576C)
            )
            Spacer(GlanceModifier.height(12.dp))

            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = GlanceModifier
                        .background(ColorProvider(Color(0xFF9C27B0)))
                        .cornerRadius(8.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable(
                            actionRunCallback<WidgetActionCallback>(
                                parameters = actionParametersOf(
                                    ActionParameters.Key<String>("action") to ControlWidget.ACTION_REFRESH_DATA
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🔄 Refresh",
                        style = TextStyle(
                            color = ColorProvider(Color.White),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
        // Status Display
//        StatusRow(
//            label = "Status:",
//            value = if (isEnabled) "ON" else "OFF",
//            valueColor = if (isEnabled) Color(0xFF4CAF50) else Color(0xFFFF5722)
//        )
//
//        StatusRow(
//            label = "USB Debugging:",
//            value = if (isUsbDebugging) "ON" else "OFF",
//            valueColor = Color(0xFF2196F3)
//        )
//
//        Spacer(modifier = GlanceModifier.height(12.dp))
//
//        // Control Buttons
//        Row(
//            modifier = GlanceModifier.fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            ControlButton(
//                text = "💡",
//                action = ControlWidget.TOGGLE_DEVELOPER_OPTIONS,
//                backgroundColor = if (isEnabled) Color(0xFF4CAF50) else Color(0xFF424242)
//            )
//
//            Spacer(modifier = GlanceModifier.width(8.dp))
//
//            ControlButton(
//                text = "⏱️",
//                action = ControlWidget.ACTION_TOGGLE_USB_DEBUGGING,
//                backgroundColor = Color(0xFF2196F3),
//                duration = "300"
//            )
//        }
//
//        Spacer(modifier = GlanceModifier.height(8.dp))
        
        // Row(
        //     modifier = GlanceModifier.fillMaxWidth(),
        //     horizontalAlignment = Alignment.CenterHorizontally
        // ) {
        //     ControlButton(
        //         text = "🔄",
        //         action = ControlWidget.ACTION_REFRESH_DATA,
        //         backgroundColor = Color(0xFF9C27B0)
        //     )
            
        //     Spacer(modifier = GlanceModifier.width(8.dp))
            
        //     ControlButton(
        //         text = "📱",
        //         action = ControlWidget.ACTION_OPEN_APP,
        //         backgroundColor = Color(0xFF607D8B)
        //     )
        // }
        
        // Spacer(modifier = GlanceModifier.height(12.dp))
        
        // // Last Update
        // Text(
        //     text = "Updated: $lastUpdate",
        //     style = TextStyle(
        //         color = ColorProvider(Color(0xFF9E9E9E)),
        //         fontSize = 10.sp
        //     ),
        //     modifier = GlanceModifier.fillMaxWidth(),
        // )
    }
}

@Composable
private fun SettingRow(
    label: String,
    description: String,
    value: Boolean,
    action: Action,
    activeColor: Color
) {
    Row(
        modifier = GlanceModifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // icon substitute: circle with emoji or text
        Box(
            modifier = GlanceModifier
                .size(36.dp)
                .background(ColorProvider(if (value) activeColor else Color(0xFFBDBDBD)))
                .cornerRadius(18.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "⚙️",
                style = TextStyle(color = ColorProvider(Color.White), fontSize = 14.sp)
            )
        }

        Spacer(GlanceModifier.width(12.dp))

        Column(modifier = GlanceModifier.defaultWeight()) {
            Text(
                text = label,
                style = TextStyle(
                    color = ColorProvider(Color(0xFF212121)),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = description,
                style = TextStyle(
                    color = ColorProvider(Color(0xFF757575)),
                    fontSize = 12.sp
                )
            )
        }

//        ToggleAction(
//            checked = value,
//            onCheckedChange = action
//        )

        Box(
            modifier = GlanceModifier
                .size(36.dp)
                .background(ColorProvider(if (value) activeColor else Color(0xFFBDBDBD)))
                .cornerRadius(18.dp)
                .clickable(action),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (value) "ON" else "OFF",
                style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun StatusRow(label: String, value: String, valueColor: Color) {
    Row(
        modifier = GlanceModifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = TextStyle(
                color = ColorProvider(Color(0xFFCCCCCC)),
                fontSize = 12.sp
            )
        )
        Spacer(modifier = GlanceModifier.width(8.dp))
        Text(
            text = value,
            style = TextStyle(
                color = ColorProvider(valueColor),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun ControlButton(
    text: String,
    action: String,
    backgroundColor: Color,
    duration: String? = null
) {
    val parameters = if (duration != null) {
        actionParametersOf(
            ActionParameters.Key<String>("action") to action,
            ActionParameters.Key<String>("duration") to duration
        )
    } else {
        actionParametersOf(
            ActionParameters.Key<String>("action") to action
        )
    }
    
    Text(
        text = text,
        style = TextStyle(
            color = ColorProvider(Color.White),
            fontSize = 16.sp
        ),
        modifier = GlanceModifier
            .background(backgroundColor)
            .cornerRadius(8.dp)
            .padding(12.dp)
            .clickable(actionRunCallback<WidgetActionCallback>(parameters))
    )
}