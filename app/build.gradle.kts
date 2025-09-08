plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.freelancekc.fdjtechnicaltest"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.freelancekc.fdjtechnicaltest"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            // We are using the testKey for debug
            buildConfigField("String", "BASE_URL", "\"https://www.thesportsdb.com/api/v1/json/123/\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // We are using the premiumKey for production
            buildConfigField("String", "BASE_URL", "\"https://www.thesportsdb.com/api/v1/json/50130162/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Module dependencies
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":presentation"))

    // Core Android
    implementation(libs.androidx.core.ktx)

    // Activity Compose
    implementation(libs.androidx.activity.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}