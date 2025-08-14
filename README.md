# Dev Ease 🚀📱🔨

**Dev Ease** is an Android utility app that simplifies enabling and managing debugging features on your Android device. Designed for developers, it allows you to quickly toggle debugging options and set debug behaviours (e.g. USB debugging, wireless debugging, etc.) from a friendly UI.

---

## ✨ Features

- Toggle **USB Debugging**
- Toggle **Wireless Debugging** (Android 11+)
- Quickly enable/disable Developer Options entirely
- View current status of each debugging option
- Simple and minimal user interface

---

## 📦 Screenshots

<img width="1080" height="2400" alt="Screenshot_1753635093" src="https://github.com/user-attachments/assets/53695825-f04d-4779-9b6e-efecb0732dca"/>

---

## 📥 Installation
1. [**Download the APK**](https://github.com/NaveedAlamDev/dev_ease/releases/latest/download/app-release.apk)
2. Install it on your Android device.
3. Connect your device to your system, make sure **ADB** is installed, and run:
   ```bash
   adb shell pm grant com.navdev.dev_ease android.permission.WRITE_SECURE_SETTINGS
4. This will allow the app to make changes to Developer Settings.

