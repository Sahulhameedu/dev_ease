import 'dart:async';
import 'package:flutter/material.dart';
import 'package:home_widget/home_widget.dart';

class GlanceWidgetController {
  static const String androidProviderName = 'ControlWidgetReceiver';
  static final StreamController<String> _actionStreamController =
      StreamController.broadcast();

  // Stream for listening to widget actions
  static Stream<String> get actionStream => _actionStreamController.stream;

  static Future<void> initialize() async {
    // Register callback for widget interactions
    await HomeWidget.registerInteractivityCallback(_handleWidgetAction);

    // Set initial widget data
    await updateWidget();
  }

  static Future<void> updateWidget({
    bool? devOptStatus,
    bool? usbDebuggingStatus,
  }) async {
    try {
      if (devOptStatus != null) {
        await HomeWidget.saveWidgetData<bool>('dev_options', devOptStatus);
      }
      if (usbDebuggingStatus != null) {
        await HomeWidget.saveWidgetData<bool>(
          'toggle_usb_debugging',
          usbDebuggingStatus,
        );
      }

      await HomeWidget.updateWidget(
        name: androidProviderName,
        androidName: androidProviderName,
      );
    } catch (e) {
      debugPrint('Error updating widget: $e');
    }
  }

  @pragma('vm:entry-point')
  static Future<void> _handleWidgetAction(Uri? uri) async {
    debugPrint('Widget action received: ${uri.toString()}');
    _actionStreamController.add(uri.toString());
  }

  static void dispose() {
    _actionStreamController.close();
  }
}
