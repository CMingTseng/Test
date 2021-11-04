plugins {
//    `kotlin-dsl`
    kotlin("jvm") version "1.5.30"
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
    mavenLocal()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
}

dependencies {
    implementation(kotlin("gradle-plugin"))
    implementation(kotlin("serialization"))
    implementation("com.android.tools.build:gradle:4.2.1")
    implementation("com.google.devtools.ksp:symbol-processing-gradle-plugin:1.5.10-1.0.0-beta01")
}

gradlePlugin {
    plugins {
        create("golang") {
            id = "clash-build"
            implementationClass = "com.github.kr328.clash.tools.ClashBuildPlugin"
        }
    }
}

kotlin {
    // Add Deps to compilation, so it will become available in main project
    //if Deps.kt no packagename must add this section let project dependencies !!
    sourceSets.getByName("main").kotlin.srcDir("buildSrc/src/main/kotlin")
}
