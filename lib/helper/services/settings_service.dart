import 'package:dev_ease/helper/constants/app_constants.dart';
import 'package:dev_ease/helper/utils/log_utils.dart';
import 'package:dev_ease/models/settings_model.dart';
import 'package:flutter/services.dart';
import 'package:toast/toast.dart';

class SettingsService {
  final _settingChannel = MethodChannel(AppConstants.settingChannel);

  Future<SettingsModel> getSettigs() async {
    try {
      final settings = await _settingChannel.invokeMethod(
        AppConstants.getSettingsMethod,
      );

      return SettingsModel.fromJson(Map<String, dynamic>.from(settings));
    } catch (e) {
      LogUtils.print(e.toString());
      Toast.show(e.toString());
      return SettingsModel();
    }
  }

  Future<SettingsModel> updateSettings(SettingsModel settings) async {
    try {
      final updatedSettings = await _settingChannel.invokeMethod(
        AppConstants.updateSettingsMethod,
        settings.toJson(),
      );

      return SettingsModel.fromJson(Map<String, dynamic>.from(updatedSettings));
    } catch (e) {
      LogUtils.print(e.toString());
      Toast.show(e.toString());
      return SettingsModel();
    }
  }
}
