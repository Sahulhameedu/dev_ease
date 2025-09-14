plugins {
    id("com.android.application")
    id("kotlin-android")
    // The Flutter Gradle Plugin must be applied after the Android and Kotlin Gradle plugins.
    id("dev.flutter.flutter-gradle-plugin")
    // For Compose Support
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
}

android {
    namespace = "com.navdev.dev_ease"
    compileSdk = 35
    ndkVersion = "28.0.12674087"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "2.1.0"
    }

    defaultConfig {
        // TODO: Specify your own unique Application ID (https://developer.android.com/studio/build/application-id.html).
        applicationId = "com.navdev.dev_ease"
        // You can update the following values to match your application needs.
        // For more information, see: https://flutter.dev/to/review-gradle-config.
        minSdk = 23
        targetSdk = 35
        versionCode = flutter.versionCode
        versionName = flutter.versionName
    }

    buildTypes {
        release {
            // TODO: Add your own signing config for the release build.
            // Signing with the debug keys for now, so `flutter run --release` works.
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

flutter {
    source = "../.."
}

dependencies {
    // implementation("androidx.glance:glance-appwidget:1.1.1")
     // For Glance support
     // implementation("androidx.glance:glance-appwidget-preview:1.1.0")
     
     // For AppWidgets support
    // implementation(platform("androidx.compose:compose-bom:2024.04.01"))
    // implementation("androidx.glance:glance-appwidget:1.1.0")
    // implementation("androidx.glance:glance-material3:1.1.0")
    // implementation("androidx.glance:glance:1.1.0")
    // implementation("androidx.compose.material3:material3:1.2.1")


    implementation(platform("androidx.compose:compose-bom:2024.04.01"))
    
    // Remove version numbers when using BOM
    implementation("androidx.glance:glance-appwidget:1.0.0-alpha05")
    implementation("androidx.glance:glance-material3:1.0.0-alpha05")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    
    // Keep specific versions for non-Compose libraries
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    // implementation("androidx.work:work-runtime-ktx:2.8.1")
}
