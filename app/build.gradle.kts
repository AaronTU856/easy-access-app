plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    // Removed: androidx.room plugin is unnecessary
    // id("androidx.room")
}

android {
    namespace = "com.example.easy_access_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.easy_access_app"
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

    // Removed: Invalid "room" configuration block
    /*
    room {
        schemaDirectory("$projectDir/schemas")
    }
    */

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.cardview:cardview:1.0.0")

    // Removed: Unnecessary older versions
    /*
    implementation ("androidx.activity:activity-ktx:1.2.0")
    implementation ("androidx.fragment:fragment-ktx:1.3.0")
    */

    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.activity)
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.fragment:fragment-ktx:1.5.7")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore")

    // Room dependencies
    val room_version = "2.5.2"

    implementation("androidx.room:room-runtime:$room_version")
    // Using KSP for Room
    ksp("androidx.room:room-compiler:$room_version")

    // Removed: Duplicate and redundant annotation processor
    /*
    annotationProcessor("androidx.room:room-compiler:$room_version")
    */

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    // optional - RxJava2 support for Room
    // Commented out unless explicitly needed
    /*
    implementation("androidx.room:room-rxjava2:$room_version")
    */

    // optional - RxJava3 support for Room
    // Commented out unless explicitly needed
    /*
    implementation("androidx.room:room-rxjava3:$room_version")
    */

    // optional - Guava support for Room, including Optional and ListenableFuture
    // Commented out unless explicitly needed
    /*
    implementation("androidx.room:room-guava:$room_version")
    */

    // optional - Test helpers
    testImplementation("androidx.room:room-testing:$room_version")

    // optional - Paging 3 Integration
    implementation("androidx.room:room-paging:$room_version")
}
