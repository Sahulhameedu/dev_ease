import 'package:dev_ease/helper/constants/app_strings.dart';
import 'package:dev_ease/presentation/screens/homeScreen/components/home_header.dart';
import 'package:dev_ease/presentation/screens/homeScreen/components/setting_item.dart';
import 'package:flutter/material.dart';
import 'package:toast/toast.dart';

import '../../../helper/services/settings_service.dart';
import '../../../models/settings_model.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> with TickerProviderStateMixin {
  late AnimationController _animationController;
  late Animation<double> _fadeAnimation;

  final _settingsService = SettingsService();
  SettingsModel _settings = SettingsModel();

  @override
  void initState() {
    super.initState();
    ToastContext().init(context);
    _initializeAimation();
    _getSettings();
  }

  @override
  void dispose() {
    _animationController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            colors: [Color(0xFF667eea), Color(0xFF764ba2)],
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
          ),
        ),
        child: SafeArea(
          child: FadeTransition(
            opacity: _fadeAnimation,
            child: Column(
              children: [
                HomeHeader(),
                Expanded(
                  child: Container(
                    decoration: const BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.only(
                        topLeft: Radius.circular(24),
                        topRight: Radius.circular(24),
                      ),
                    ),
                    child: Padding(
                      padding: const EdgeInsets.all(24),
                      child: Column(
                        spacing: 16,
                        children: [
                          SettingItem(
                            icon: Icons.code,
                            iconColor: const LinearGradient(
                              colors: [Color(0xFF667eea), Color(0xFF764ba2)],
                            ),
                            title: AppStrings.developerOptions,
                            subtitle: AppStrings.developerOptionsDesc,
                            value: _settings.devModeEnabled,
                            onChanged: (value) {
                              _updateSettings(
                                _settings.copyWith(
                                  devModeEnabled: !_settings.devModeEnabled,
                                ),
                              );
                            },
                            delay: 100,
                          ),
                          SettingItem(
                            icon: Icons.usb,
                            iconColor: const LinearGradient(
                              colors: [Color(0xFFf093fb), Color(0xFFf5576c)],
                            ),
                            title: AppStrings.usbDebugging,
                            subtitle: AppStrings.usbDebuggingDesc,
                            value: _settings.adbEnabled,
                            onChanged: (value) {
                              _updateSettings(
                                _settings.copyWith(
                                  adbEnabled: !_settings.adbEnabled,
                                ),
                              );
                            },
                            delay: 200,
                          ),
                          SettingItem(
                            icon: Icons.wifi,
                            iconColor: const LinearGradient(
                              colors: [Color(0xFF4facfe), Color(0xFF00f2fe)],
                            ),
                            title: AppStrings.wirelessDebugging,
                            subtitle: AppStrings.wirelessDebuggingDesc,
                            value: _settings.wifiAdbEnabled,
                            showSettingsIcon: true,
                            onChanged: (value) {
                              _updateSettings(
                                _settings.copyWith(
                                  wifiAdbEnabled: !_settings.wifiAdbEnabled,
                                ),
                              );
                            },
                            delay: 300,
                          ),
                        ],
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  _initializeAimation() {
    _animationController = AnimationController(
      duration: const Duration(milliseconds: 800),
      vsync: this,
    );
    _fadeAnimation = Tween<double>(begin: 0.0, end: 1.0).animate(
      CurvedAnimation(parent: _animationController, curve: Curves.easeOut),
    );
    _animationController.forward();
  }

  _getSettings() async {
    _settings = await _settingsService.getSettigs();
    setState(() {});
  }

  _updateSettings(SettingsModel settings) async {
    _settings = await _settingsService.updateSettings(settings);
    setState(() {});
  }
}
