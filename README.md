# Dev Ease 🚀📱🔨

**Dev Ease** is an Android utility app that simplifies enabling and managing debugging features on your Android device. Designed for developers, it allows you to quickly toggle debugging options and set debug behaviours (e.g. USB debugging, wireless debugging, etc.) from a friendly UI.

---

## ✨ Features

- Toggle **USB Debugging** (from Developer Options)
- Toggle **Wireless Debugging** (Android 11+)
- Quickly enable/disable Developer Options entirely
- View current status of each debugging option
- Optionally apply settings persistently
- Simple and minimal user interface

---

## 📦 Screenshots

<img width="1080" height="2400" alt="Screenshot_1753635093" src="https://github.com/user-attachments/assets/fbdef3c9-37cb-4667-8474-52ec5b5c10cd" />

---

## 📥 Installation
1. [**Download the latest APK**](https://github.com/USERNAME/REPO/releases/latest/download/app-release.apk)
2. Install it on your Android device.
3. Connect your device to your system, make sure **ADB** is installed, and run:
   ```bash
   adb shell pm revoke com.navdev.dev_ease android.permission.WRITE_SECURE_SETTINGS

