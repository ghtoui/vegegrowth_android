import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.moritoui.vegegrowthapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.moritoui.vegegrowthapp"
        minSdk = 28
        targetSdk = 34
        versionCode = 19
        versionName = "2.9"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        val propertiesFile = project.rootProject.file("private.properties")
        val properties = Properties()
        properties.load(FileInputStream(propertiesFile))

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField(
                "String",
                "AD_BANNER_UNIT_ID",
                "${properties["RELEASE_AD_BANNER_UNIT_ID"]}"
            )
        }
        debug {
            buildConfigField(
                "String",
                "AD_BANNER_UNIT_ID",
                "${properties["DEBUG_AD_BANNER_UNIT_ID"]}"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

ksp {
    /**
     * schema定義を出力しておくと、AutoMigrationできるようになる
     */
    arg("room.schemaLocation", "$projectDir/schemas")
}

val ktlint by configurations.creating

dependencies {
    ktlint("com.pinterest.ktlint:ktlint-cli:1.3.0") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
}

val ktlintCheck by tasks.registering(JavaExec::class) {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
    args(
        "**/src/**/*.kt",
        "**.kts",
        "!**/build/**"
    )
}

tasks.check {
    dependsOn(ktlintCheck)
}

tasks.register<JavaExec>("ktlintFormat") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style and format"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
    // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
    args(
        "-F",
        "**/src/**/*.kt",
        "**.kts",
        "!**/build/**"
    )
}

dependencies {
    implementation(libs.activity.compose)
    implementation(libs.androidx.ktx)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions)
    implementation(libs.accompanist.permissions)
    implementation(libs.truth)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.lottie.compose)
    implementation(libs.play.services.ads)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.timber)
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.retrofit.gson)
}
