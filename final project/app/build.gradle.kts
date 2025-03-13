plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
}

android {
    namespace = "ddwu.com.mobileapplication.finalproject"
    compileSdk = 35

    defaultConfig {
        applicationId = "ddwu.com.mobileapplication.finalproject"
        minSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    viewBinding{
        enable=true
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
    // 필요한 의존성 추가
    implementation ("androidx.activity:activity-ktx:1.7.0")  // viewModels를 위한 KTX 확장 함수 제공
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")  // ViewModel 사용을 위한 KTX 지원


    // ROOM
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // To use Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:$room_version")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    // Lifecycle components
    val lifecycle_version = "2.8.5"
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation ("androidx.lifecycle:lifecycle-common-java8:$lifecycle_version")

    // retrofit
    val retrofit_version = "2.11.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    //XML의 경우
    implementation("com.squareup.retrofit2:converter-simplexml:2.7.1")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Google Play Services
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // GoogleMap
    implementation ("com.google.android.gms:play-services-maps:19.0.0")
}