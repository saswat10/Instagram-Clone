import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.0.21"
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use {
        localProperties.load(localPropertiesFile.inputStream())
    }
}


android {
    namespace = "com.saswat10.instagramclone"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.saswat10.instagramclone"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        vectorDrawables{
            useSupportLibrary = true
        }

        buildConfigField(
            "String",
            "API_ENV_VARIABLE",
            "\"${localProperties.getProperty("API_ENV_VARIABLE")}\""
        )
        buildConfigField(
            "String",
            "CLOUDINARY_CLOUD_NAME",
            "\"${localProperties.getProperty("CLOUDINARY_CLOUD_NAME")}\""
        )
        buildConfigField(
            "String",
            "CLOUDINARY_API_KEY",
            "\"${localProperties.getProperty("CLOUDINARY_API_KEY")}\""
        )
        buildConfigField(
            "String",
            "CLOUDINARY_API_SECRET",
            "\"${localProperties.getProperty("CLOUDINARY_API_SECRET")}\""
        )
        buildConfigField(
            "String",
            "CLOUDINARY_UPLOAD_PRESET",
            "\"${localProperties.getProperty("CLOUDINARY_UPLOAD_PRESET")}\""
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation("androidx.core:core-splashscreen:1.0.0")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.8.1")

    implementation(platform("com.google.firebase:firebase-bom:33.15.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("androidx.credentials:credentials:1.3.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
    implementation("com.cloudinary:cloudinary-android:2.8.0")
    implementation(libs.play.services.auth)


    implementation("com.google.dagger:hilt-android:2.56.2")
    implementation(libs.firebase.firestore.ktx)
    ksp("com.google.dagger:hilt-android-compiler:2.56.2")

    implementation("androidx.hilt:hilt-navigation-fragment:1.0.0")
    implementation("androidx.navigation:navigation-compose:2.9.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
    implementation("androidx.compose.material3.adaptive:adaptive")
    implementation( "com.jakewharton.timber:timber:5.0.1")
    implementation("io.coil-kt.coil3:coil-compose:3.2.0")
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.viewmodel.compose)



    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

