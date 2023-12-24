plugins {
    id("com.android.application") version "8.1.3"
    id("org.jetbrains.kotlin.android") version "1.9.20"
    id("com.google.devtools.ksp") version "1.9.21-1.0.15"
}

android {
    namespace = "epicarchitect.nice"
    compileSdk = 34

    defaultConfig {
        applicationId = "epicarchitect.nice"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("com.github.epicarchitect:epic-adapter:1.0.9")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
}
