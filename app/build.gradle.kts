plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.capstone"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.capstone"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation ("com.theartofdev.edmodo:android-image-cropper:2.8.0")
    implementation ("com.google.android.gms:play-services-vision:20.1.3")
    implementation ("com.google.android.gms:play-services-location:19.0.1")
    implementation ("com.github.DantSu:ESCPOS-ThermalPrinter-Android:3.3.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.airbnb.android:lottie:5.2.0")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.github.gcacace:signature-pad:1.3.1")
    implementation ("com.squareup.picasso:picasso:2.8")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.1.1")
    implementation("com.google.firebase:firebase-storage:20.2.1")
    implementation("com.google.firebase:firebase-database-ktx:20.2.2")
    implementation("com.google.firebase:firebase-auth-ktx:22.1.2")
    implementation("com.google.firebase:firebase-storage-ktx:20.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}