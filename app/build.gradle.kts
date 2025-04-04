import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    id("kotlin-parcelize")

}

android {
    namespace = "net.runner.fitbit"
    compileSdk = 35

    defaultConfig {
        applicationId = "net.runner.fitbit"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        val properties =  Properties()
        properties.load(project.rootProject.file("apikeys.properties").inputStream())
        buildConfigField("String","G_OAUTH_WEB_SERVER_CLIENT_ID","\"${properties.getProperty("G_OAUTH_WEB_SERVER_CLIENT_ID","")}\"")
        buildConfigField("String","GENERATIVE_API_KEY","\"${properties.getProperty("GENERATIVE_API_KEY","")}\"")
        buildConfigField("String","DISTANCE_MATRIX_API_KEY","\"${properties.getProperty("DISTANCE_MATRIX_API_KEY","")}\"")
        buildConfigField("String","ONESIGNAL_APP_ID","\"${properties.getProperty("ONESIGNAL_APP_ID","")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            val properties =  Properties()
            properties.load(project.rootProject.file("apikeys.properties").inputStream())
            buildConfigField("String","G_OAUTH_WEB_SERVER_CLIENT_ID","\"${properties.getProperty("G_OAUTH_WEB_SERVER_CLIENT_ID","")}\"")
            buildConfigField("String","GENERATIVE_API_KEY","\"${properties.getProperty("GENERATIVE_API_KEY","")}\"")
            buildConfigField("String","DISTANCE_MATRIX_API_KEY","\"${properties.getProperty("DISTANCE_MATRIX_API_KEY","")}\"")
            buildConfigField("String","ONESIGNAL_APP_ID","\"${properties.getProperty("ONESIGNAL_APP_ID","")}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(libs.firebase.crashlytics)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.material)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.play.services.location)
    implementation(libs.generativeai)
    implementation(libs.firebase.messaging)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")

    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation ("io.github.shashank02051997:FancyToast:2.0.2")
    implementation (libs.play.services.maps)
    implementation ("com.onesignal:OneSignal:[5.0.0, 5.99.99]")
        implementation("io.socket:socket.io-client:2.0.1")
        implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("io.github.hussein-al-zuhile:Shimmery:1.2.1")

}