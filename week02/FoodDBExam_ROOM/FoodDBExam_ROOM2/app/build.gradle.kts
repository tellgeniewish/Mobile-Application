plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    // ksp 추가
    // @ 어노태이션 처리
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
}

android {
    namespace = "ddwu.com.mobileapp.week02.fooddbexam_room"
    compileSdk = 34

    defaultConfig {
        applicationId = "ddwu.com.mobileapp.week02.fooddbexam_room"
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
    viewBinding {
        enable=true
    }
}

dependencies {

    // kts를 사용하지 않으면 쓰는 방식이 달라짐 (괄호가 생략된다던지)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

//    ROOM
    // ROOM을 쓸 때 3가지 필수
    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // 둘 중에 하나 사용
    // To use Kotlin annotation processing tool (kapt)
//    kapt("androidx.room:room-compiler:$room_version")
    // To use Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:$room_version") // 코틀린 dsl이 아니어도 사용 ㄱㄴ 하지만 쓰는 방식이 달라짐
    // sync하면 ROOM과 관련된 라이브러리 사용 ㄱㄴ

    // optional - Kotlin Extensions and Coroutines support for Room
//    implementation("androidx.room:room-ktx:$room_version")


}