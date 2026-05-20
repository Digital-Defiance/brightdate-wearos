import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

// Load release signing credentials from a local, gitignored file.
val keystoreProperties = Properties().apply {
    val file = rootProject.file("keystore.properties")
    if (file.exists()) load(file.inputStream())
}

android {
    // Tell AGP this is a no-code project (WFF watch face).
    // This prevents dex generation entirely.
    // enableKotlin = false // This property is not recognized by AGP and is causing an error.
    // The `android:hasCode="false"` in AndroidManifest.xml already indicates no code.

    namespace = "org.brightchain.brightdate"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.brightchain.brightdate"
        minSdk = 33
        targetSdk = 34
        versionCode = 11
        versionName = "1.1.1"
    }

    signingConfigs {
        create("release") {
            if (keystoreProperties.isNotEmpty()) {
                storeFile = rootProject.file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false // Set to false for debug builds
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = false
            signingConfig = if (keystoreProperties.isNotEmpty()) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }
        }
    }
}