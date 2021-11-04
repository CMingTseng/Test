import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("application")
    kotlin("jvm")
    `maven-publish`
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType(KotlinCompile::class) {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

publishing {
    publications {
        create("release", type = MavenPublication::class) {
            pom {
                name.set("kaidl")
                description.set("Generate AIDL-like android binder interface with Kotlin")
                url.set("https://github.com/Kr328/kaidl")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("http://www.opensource.org/licenses/mit-license.php")
                    }
                }
                developers {
                    developer {
                        id.set("kr328")
                        name.set("Kr328")
                        email.set("kr328app@outlook.com")
                    }
                }
            }

            from(components["java"])

            groupId = "com.github.kr328.kaidl"
            artifactId = "kaidl"

            version = "2.4.6"
        }
    }

    repositories {
        maven {
            url = uri("${rootProject.buildDir}/../release")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.5.10-1.0.0-beta01")
    implementation("com.squareup:kotlinpoet:1.8.0")
}
