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
import androidx.glance.Image
import androidx.glance.ImageProvider
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
import com.navdev.dev_ease.R

class ControlWidget : GlanceAppWidget() {
    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    companion object {
        const val TOGGLE_DEVELOPER_OPTIONS = "toggle_dev_options"
        const val ACTION_TOGGLE_USB_DEBUGGING = "toggle_usb_debugging"
        const val ACTION_REFRESH_DATA = "refresh_data"
        val IS_ENABLED_KEY = booleanPreferencesKey("dev_options_enabled")
        val USB_DEBUGGING_KEY = booleanPreferencesKey("usb_debugging_enabled")

        fun isDeveloperOptionsEnabled(context: Context): Boolean =
            try {
                Settings.Global.getInt(
                    context.contentResolver,
                    Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
                    0,
                ) == 1
            } catch (e: Exception) {
                false
            }

        fun isUsbDebuggingEnabled(context: Context): Boolean =
            try {
                Settings.Global.getInt(
                    context.contentResolver,
                    Settings.Global.ADB_ENABLED,
                    0,
                ) == 1
            } catch (e: Exception) {
                false
            }
    }

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
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

    suspend fun refreshAllWidgets(context: Context) {
        updateAll(context)
    }
}

@Composable
private fun WidgetContent(context: Context) {
    val prefs = currentState<androidx.datastore.preferences.core.Preferences>()
    val devEnabled = prefs[ControlWidget.IS_ENABLED_KEY] ?: false
    val usbEnabled = prefs[ControlWidget.USB_DEBUGGING_KEY] ?: false

    Box(
        modifier =
            GlanceModifier
                .fillMaxWidth()
                .background(ColorProvider(Color(0x33FFFFFF)))
                .cornerRadius(24.dp)
                .padding(2.dp),
    ) {
        Column(
            modifier =
                GlanceModifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(ColorProvider(Color(0x66FFFFFF)))
                    .cornerRadius(24.dp)
                    .padding(16.dp),
        ) {
            // Header
            Row(
                modifier =
                    GlanceModifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.Start,
            ) {
                // Code icon with bluish glass bg
                Box(
                    modifier =
                        GlanceModifier
                            .size(36.dp)
                            .background(ColorProvider(Color(0x2D64A0FF)))
                            .cornerRadius(10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        provider = ImageProvider(R.drawable.code),
                        contentDescription = "Developer Icon",
                        modifier = GlanceModifier.size(18.dp),
                    )
                }
                Spacer(modifier = GlanceModifier.width(10.dp))
                Text(
                    text = "Developer Controls",
                    modifier = GlanceModifier.defaultWeight(),
                    style =
                        TextStyle(
                            color = ColorProvider(Color(0xFF000000)),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                        ),
                )
                // Rounded refresh button
                Box(
                    modifier =
                        GlanceModifier
                            .size(36.dp)
                            .background(ColorProvider(Color(0xFFFFFFFF)))
                            .cornerRadius(50.dp)
                            .clickable(
                                actionRunCallback<WidgetActionCallback>(
                                    parameters =
                                        actionParametersOf(
                                            ActionParameters.Key<String>("action") to ControlWidget.ACTION_REFRESH_DATA,
                                        ),
                                ),
                            ),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        provider = ImageProvider(R.drawable.refresh),
                        contentDescription = "Refresh",
                        modifier = GlanceModifier.size(16.dp),
                    )
                }
            }

            // Developer Options row
            SettingRow(
                label = "Developer Options",
                description = "Enable Android Developer Options",
                value = devEnabled,
                action =
                    actionRunCallback<WidgetActionCallback>(
                        parameters =
                            actionParametersOf(
                                ActionParameters.Key<String>("action") to ControlWidget.TOGGLE_DEVELOPER_OPTIONS,
                            ),
                    ),
                activeColor = Color(0xFF475AD7),
            )
            Spacer(GlanceModifier.height(10.dp))

            // USB Debugging row
            SettingRow(
                label = "USB Debugging",
                description = "Debug over USB",
                value = usbEnabled,
                action =
                    actionRunCallback<WidgetActionCallback>(
                        parameters =
                            actionParametersOf(
                                ActionParameters.Key<String>("action") to ControlWidget.ACTION_TOGGLE_USB_DEBUGGING,
                            ),
                    ),
                activeColor = Color(0xFF475AD7),
            )
        }
    }
}

@Composable
private fun SettingRow(
    label: String,
    description: String,
    value: Boolean,
    action: Action,
    activeColor: Color,
) {
    Row(
        modifier =
            GlanceModifier
                .fillMaxWidth()
                .background(ColorProvider(Color(0xFFFFFFFF)))
                .cornerRadius(20.dp)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = GlanceModifier.defaultWeight(),
        ) {
            Text(
                text = label,
                style =
                    TextStyle(
                        color = ColorProvider(Color(0xFF212121)),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    ),
            )

            Spacer(GlanceModifier.height(2.dp))

            Text(
                text = description,
                style =
                    TextStyle(
                        color = ColorProvider(Color(0xFF757575)),
                        fontSize = 12.sp,
                    ),
            )
        }

        Box(
            modifier =
                GlanceModifier
                    .width(50.dp)
                    .height(28.dp)
                    .background(
                        ColorProvider(
                            if (value) activeColor else Color(0xFFBDBDBD),
                        ),
                    ).cornerRadius(50.dp)
                    .clickable(action)
                    .padding(4.dp),
            contentAlignment = if (value) Alignment.CenterEnd else Alignment.CenterStart,
        ) {
            Box(
                modifier =
                    GlanceModifier
                        .size(20.dp)
                        .background(ColorProvider(Color.White))
                        .cornerRadius(50.dp)
                        .then(
                            if (value) {
                                GlanceModifier.padding(start = 22.dp)
                            } else {
                                GlanceModifier.padding(start = 0.dp)
                            },
                        ),
            ) {}
        }
    }
}

@Composable
private fun StatusRow(
    label: String,
    value: String,
    valueColor: Color,
) {
    Row(
        modifier =
            GlanceModifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            style =
                TextStyle(
                    color = ColorProvider(Color(0xFFCCCCCC)),
                    fontSize = 12.sp,
                ),
        )
        Spacer(modifier = GlanceModifier.width(8.dp))
        Text(
            text = value,
            style =
                TextStyle(
                    color = ColorProvider(valueColor),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                ),
        )
    }
}

@Composable
private fun ControlButton(
    text: String,
    action: String,
    backgroundColor: Color,
    duration: String? = null,
) {
    val parameters =
        if (duration != null) {
            actionParametersOf(
                ActionParameters.Key<String>("action") to action,
                ActionParameters.Key<String>("duration") to duration,
            )
        } else {
            actionParametersOf(
                ActionParameters.Key<String>("action") to action,
            )
        }

    Text(
        text = text,
        style =
            TextStyle(
                color = ColorProvider(Color.White),
                fontSize = 16.sp,
            ),
        modifier =
            GlanceModifier
                .background(backgroundColor)
                .cornerRadius(8.dp)
                .padding(12.dp)
                .clickable(actionRunCallback<WidgetActionCallback>(parameters)),
    )
}
