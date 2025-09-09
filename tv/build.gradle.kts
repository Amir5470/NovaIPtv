plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.project.novaiptv"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.project.novaiptv"
        minSdk = 21
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
    // It's good practice to enable viewBinding
    buildFeatures {
        viewBinding = true
    }
}

repositories {
    google()
    mavenCentral()
    jcenter() // Optional
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.leanback)
    implementation(libs.glide)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.appcompat) // For attributes like colorPrimary, actionBarSize and AppCompat themes
    implementation(libs.androidx.constraintlayout) // For ConstraintLayout attributes
    implementation(libs.material) // Material Components often provides enhanced versions of these and is generally recommended
    implementation("com.github.mmin18:RealtimeBlurView:1.2.1") // Corrected RealtimeBlurView library coordinates

}
