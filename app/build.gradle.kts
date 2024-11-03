// Подключаем необходимые плагины
plugins {
    alias(libs.plugins.android.application)  // Плагин для сборки Android приложения
    alias(libs.plugins.kotlin.android)       // Плагин для поддержки Kotlin
}

android {
    // Основные настройки приложения
    namespace = "com.yuliitezary.gpt"        // Пространство имен приложения
    compileSdk = 34                          // Версия Android SDK для компиляции

    // Конфигурация по умолчанию
    defaultConfig {
        applicationId = "com.yuliitezary.gpt" // Уникальный идентификатор приложения
        minSdk = 26                          // Минимальная поддерживаемая версия Android
        targetSdk = 34                       // Целевая версия Android
        versionCode = 36                     // Код версии для Google Play
        versionName = "2.1.0"               // Отображаемая версия приложения

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // Тестовый runner
    }

    // Конфигурация подписи приложения
    signingConfigs {
        create("release") {
            storeFile = file("yuliitezarygml.jks")    // Путь к keystore файлу
            storePassword = "Iphone@multi1"              // Пароль хранилища
            keyAlias = "yuliitezarygml"                          // Алиас ключа
            keyPassword = "Iphone@multi1"                // Пароль ключа
        }
    }

    // Настройки сборки для разных типов (release и debug)
    buildTypes {
        // Релизная сборка
        release {
            isMinifyEnabled = true           // Включаем минификацию кода
            isShrinkResources = true         // Включаем сжатие ресурсов
            proguardFiles(                   // Настройки ProGuard/R8
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release") // Используем релизную подпись
        }
        // Отладочная сборка
        debug {
            isMinifyEnabled = false          // Отключаем минификацию для отладки
            isShrinkResources = false        // Отключаем сжатие ресурсов для отладки
            signingConfig = signingConfigs.getByName("debug") // Используем отладочную подпись
        }
    }

    // Настройки совместимости с Java
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11    // Версия исходного кода
        targetCompatibility = JavaVersion.VERSION_11    // Версия байткода
    }

    // Настройки Kotlin
    kotlinOptions {
        jvmTarget = "11"                    // Версия JVM для Kotlin
    }

    // Настройки упаковки приложения
    packaging {
        resources {
            // Исключаем ненужные файлы из APK
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/LICENSE*"
            excludes += "META-INF/NOTICE*"
        }
    }
}

// Зависимости проекта
dependencies {
    // Основные библиотеки AndroidX
    implementation(libs.androidx.core.ktx)           // Основные Kotlin расширения
    implementation(libs.androidx.appcompat)          // Поддержка старых версий Android
    implementation(libs.material)                    // Material Design компоненты
    implementation(libs.androidx.activity)           // Поддержка Activity
    implementation(libs.androidx.constraintlayout)   // ConstraintLayout для UI
    implementation(libs.play.services.ads.lite)      // Облегченная версия рекламы

    // Библиотеки для тестирования
    testImplementation(libs.junit)                   // JUnit для unit-тестов
    androidTestImplementation(libs.androidx.junit)   // JUnit для Android-тестов
    androidTestImplementation(libs.androidx.espresso.core) // Espresso для UI-тестов
}