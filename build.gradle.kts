plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.20"
    id("org.jetbrains.intellij.platform") version "2.10.1"
}

group = "org.livitbox"
version = "1.9"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

// Configure IntelliJ Platform Gradle Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
dependencies {
    intellijPlatform {
        create("IC", "2024.2.6")
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
        testImplementation("junit:junit:4.13.2") // Legacy JUnit 4 for Platform compatibility
        testImplementation(platform("org.junit:junit-bom:5.14.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        testImplementation("org.mockito:mockito-core:5.20.0")
        testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
        testImplementation("com.google.jimfs:jimfs:1.3.1")

        // Add necessary plugin dependencies for compilation here, example:
        // bundledPlugin("com.intellij.java")
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "242"
        }
    }
    pluginVerification {
        ides {
            create("IC", "2025.2")
            create("IC", "2025.1.6")
            create("IC", "2024.3.7")
            create("IC", "2024.2.6")
        }
    }
    publishing {
        token = providers.environmentVariable("JETBRAINS_TOKEN")
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
    test {
        useJUnitPlatform()
    }
    buildPlugin {
        dependsOn(test)
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}
