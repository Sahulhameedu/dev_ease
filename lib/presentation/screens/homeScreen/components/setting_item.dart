import 'package:android_intent_plus/android_intent.dart';
import 'package:android_intent_plus/flag.dart';
import 'package:dev_ease/helper/constants/app_constants.dart';
import 'package:dev_ease/helper/constants/app_strings.dart';
import 'package:dev_ease/helper/utils/log_utils.dart';
import 'package:dev_ease/presentation/screens/homeScreen/components/switch_button.dart';
import 'package:flutter/material.dart';
import 'package:toast/toast.dart';

class SettingItem extends StatelessWidget {
  final IconData icon;
  final LinearGradient iconColor;
  final String title;
  final String subtitle;
  final bool value;
  final ValueChanged<bool> onChanged;
  final int delay;
  final bool showSettingsIcon;

  const SettingItem({
    super.key,
    required this.icon,
    required this.iconColor,
    required this.title,
    required this.subtitle,
    required this.value,
    required this.onChanged,
    this.delay = 100,
    this.showSettingsIcon = false,
  });

  @override
  Widget build(BuildContext context) {
    return TweenAnimationBuilder<double>(
      duration: Duration(milliseconds: 600 + delay),
      tween: Tween<double>(begin: 0.0, end: 1.0),
      curve: Curves.easeOutBack,
      builder: (context, animationValue, child) {
        return Transform.translate(
          offset: Offset(0, 20 * (1 - animationValue)),
          child: Opacity(opacity: animationValue.clamp(0, 1), child: child),
        );
      },
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 200),
        padding: const EdgeInsets.all(20),
        decoration: BoxDecoration(
          gradient: const LinearGradient(
            colors: [Colors.white, Color(0xFFf8f9ff)],
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
          ),
          borderRadius: BorderRadius.circular(16),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withValues(alpha: .08),
              blurRadius: 20,
              offset: const Offset(0, 4),
            ),
          ],
          border: Border.all(
            color: const Color(0xFF4f46e5).withValues(alpha: .1),
          ),
        ),
        child: Column(
          children: [
            Row(
              children: [
                Container(
                  width: 48,
                  height: 48,
                  decoration: BoxDecoration(
                    gradient: iconColor,
                    borderRadius: BorderRadius.circular(12),
                    boxShadow: [
                      BoxShadow(
                        color: iconColor.colors.first.withValues(alpha: .3),
                        blurRadius: 12,
                        offset: const Offset(0, 4),
                      ),
                    ],
                  ),
                  child: Icon(icon, color: Colors.white, size: 24),
                ),
                const SizedBox(width: 16),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        title,
                        style: const TextStyle(
                          fontSize: 18,
                          fontWeight: FontWeight.w600,
                          color: Color(0xFF1a202c),
                        ),
                      ),
                      const SizedBox(height: 4),
                      Text(
                        subtitle,
                        style: const TextStyle(
                          fontSize: 14,
                          color: Color(0xFF718096),
                        ),
                      ),
                    ],
                  ),
                ),
                const SizedBox(width: 16),
                SwitchButton(value: value, onChanged: onChanged),
              ],
            ),
            SizedBox(height: 10),
            if (showSettingsIcon) ...[
              GestureDetector(
                onTap: _openWirelessDebuggingSettings,
                child: AnimatedSize(
                  duration: Duration(milliseconds: 200),
                  child: Container(
                    height: value ? null : 0,
                    padding: const EdgeInsets.all(8),
                    decoration: BoxDecoration(
                      color: Colors.grey[200],
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: Row(
                      mainAxisSize: MainAxisSize.min,
                      spacing: 10,
                      children: [
                        Icon(
                          Icons.qr_code_scanner_outlined,
                          size: value ? null : 0,
                        ),
                        Text(
                          AppStrings.openScanner,
                          style: const TextStyle(
                            fontSize: 12,
                            fontWeight: FontWeight.w500,
                            color: Color(0xFF1a202c),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ),
              const SizedBox(width: 12),
            ],
          ],
        ),
      ),
    );
  }

  void _openWirelessDebuggingSettings() async {
    try {
      final intent = AndroidIntent(
        action: AppConstants.wirelessDebuggingScreen,
        flags: <int>[Flag.FLAG_ACTIVITY_NEW_TASK],
      );
      await intent.launch();
    } catch (e) {
      LogUtils.print(e.toString());
      try {
        final intent = AndroidIntent(
          action: AppConstants.developerOptionsScreen,
          flags: <int>[Flag.FLAG_ACTIVITY_NEW_TASK],
        );
        await intent.launch();
      } catch (e) {
        LogUtils.print(e.toString());
        Toast.show(AppStrings.failedToOpenSettings);
      }
    }
  }
}
