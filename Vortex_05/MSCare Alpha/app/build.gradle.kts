plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.mscarealpha"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mscarealpha"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // AndroidX and Material Design
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0") // Use the latest version
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Lifecycle and ViewModel
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // Navigation
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")

    // Legacy Support
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    // Networking
    implementation("com.android.volley:volley:1.2.1") // For API calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Retrofit for API calls
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Gson converter for Retrofit

    // Location Services
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // Shimmer Effect
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // Kotlin Standard Library
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")

    implementation ("com.loopj.android:android-async-http:1.4.11") // Add this line
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}