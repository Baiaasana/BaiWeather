plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("androidx.navigation.safeargs")
    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.baiweather"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.baiweather"
        minSdk = 24
        targetSdk = 34
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
    kotlinOptions {
        jvmTarget = "1.8"

    }
  buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)



    // hilt
    implementation (libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kapt (libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.fragment)
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)
    // location
    implementation (libs.play.services.location)

    // retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.moshi)

    //okhttp
    implementation (libs.okhttp)
    implementation (libs.logging.interceptor)

    // lifecycle
    implementation (libs.androidx.lifecycle.viewmodel.compose)

    // timber
    implementation (libs.timber)

    //glide
    implementation (libs.glide)


    // viewpager2
    implementation(libs.androidx.viewpager2)

    implementation (libs.androidx.datastore.preferences)
    implementation ("com.google.code.gson:gson:2.10.1")


}
//
//kapt {
//    correctErrorTypes true
//}