rootProject.name = "libgdxplugin"
pluginManagement {
    repositories {
        maven {
            url = java.net.URI("https://oss.sonatype.org/content/repositories/snapshots/")
        }
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
