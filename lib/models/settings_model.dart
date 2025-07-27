class SettingsModel {
  final bool devModeEnabled;
  final bool adbEnabled;
  final bool wifiAdbEnabled;

  SettingsModel({
    this.devModeEnabled = false,
    this.adbEnabled = false,
    this.wifiAdbEnabled = false,
  });

  factory SettingsModel.fromJson(Map<String, dynamic> json) {
    return SettingsModel(
      devModeEnabled: json['devModeEnabled'] as bool,
      adbEnabled: json['adbEnabled'] as bool,
      wifiAdbEnabled: json['wifiAdbEnabled'] as bool,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'devModeEnabled': devModeEnabled,
      'adbEnabled': adbEnabled,
      'wifiAdbEnabled': wifiAdbEnabled,
    };
  }

  SettingsModel copyWith({
    bool? devModeEnabled,
    bool? adbEnabled,
    bool? wifiAdbEnabled,
  }) {
    return SettingsModel(
      devModeEnabled: devModeEnabled ?? this.devModeEnabled,
      adbEnabled: adbEnabled ?? this.adbEnabled,
      wifiAdbEnabled: wifiAdbEnabled ?? this.wifiAdbEnabled,
    );
  }
}
