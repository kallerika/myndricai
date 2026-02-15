plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.firebase.google.services)
}

android {
    namespace = "com.example.myndricai"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.myndricai"
        minSdk = 28
        targetSdk = 36
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Firebase Auth
    implementation(libs.firebase.auth)
    implementation(libs.firebase.auth.google)

    // Firebase Firestore
    implementation(libs.firebase.firestore)

    // RecyclerView
    implementation(libs.recyclerview)

    // Glide для загрузки изображений (если понадобится)
    implementation(libs.glide)

    // Для работы с JSON (если нужно будет парсить данные)
    implementation(libs.gson)

    // Room Database (для кэширования данных, если потребуется)
    val room_version = "2.8.3"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.gridlayout:gridlayout:1.0.0")


    // Дополнительные библиотеки
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.google.android.gms:play-services-auth:21.1.1")


    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}