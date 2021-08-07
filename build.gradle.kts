import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key).toString()

plugins {
  id("java")
  id("maven-publish")
  id("org.jetbrains.kotlin.jvm") version "1.5.10"
  id("org.jetbrains.intellij") version "1.0"
  id("io.gitlab.arturbosch.detekt") version "1.17.1"
  id("com.github.blueboxware.tocme") version "1.3"
}

group = properties("pluginGroup")
version = properties("pluginVersion")

repositories {
  mavenCentral()
}

intellij {
  pluginName.set(properties("pluginName"))
  version.set(properties("platformVersion"))
  type.set(properties("platformType"))
  downloadSources.set(properties("platformDownloadSources").toBoolean())
  updateSinceUntilBuild.set(true)

  // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
  plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
}

detekt {
  config = files("./detekt-config.yml")
  baseline = file("./detekt-baseline.xml")
  buildUponDefaultConfig = true

  reports {
    html.enabled = true
    xml.enabled = false
    txt.enabled = false
    sarif.enabled = false
  }
}

sourceSets {
  main {
    java.srcDirs("gen")
    java.srcDirs("src/main/kotlin")
    java.exclude("com/gmail/blueboxware/libgdxplugin/annotations/**")
    resources.srcDirs("resources")
  }

  register("annotations") {
    java.srcDirs("src/main/java")
  }
}

tasks {
  // Set the compatibility versions to 1.8
  withType<JavaCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
  }
  withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.apiVersion = "1.4"
  }

  withType<Detekt> {
    jvmTarget = "1.8"
  }

  runIde {
    maxHeapSize = "2g"
    systemProperties = mapOf(
      "idea.ProcessCanceledException" to "disabled"
    )
  }

  tocme {
    doc("README.md")
  }

  buildSearchableOptions {
    // TODO
    enabled = false
  }

  patchPluginXml {
    version.set(properties("pluginVersion"))
    sinceBuild.set(properties("pluginSinceBuild"))
    untilBuild.set(properties("pluginUntilBuild"))
  }

  runPluginVerifier {
    ideVersions.set(properties("pluginVerifierIdeVersions").split(',').map(String::trim).filter(String::isNotEmpty))
  }

  test {
    systemProperty("idea.home.path", System.getenv("LIBGDXPLUGIN_IDEA"))
  }

  register<Jar>("annotationsJar") {
    archiveBaseName.set("libgdxpluginannotations")
    from(sourceSets.getByName("annotations").output)
    include("com/gmail/blueboxware/libgdxplugin/annotations/**")
    archiveVersion.set(properties("pluginVersion"))
  }

  register<Jar>("annotationsSourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("annotations").allSource)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    include(project.tasks.getByName<Jar>("annotationsJar").includes)
    archiveBaseName.set(project.tasks.getByName<Jar>("annotationsJar").archiveBaseName)
    archiveVersion.set(properties("pluginVersion"))
  }

}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      artifactId = "libgdxplugin"
      artifact(tasks.getByName("annotationsJar"))
      artifact(tasks.getByName("annotationsSourcesJar"))
      versionMapping {
        usage("java-api") {
          fromResolutionOf("runtimeClasspath")
        }
        usage("java-runtime") {
          fromResolutionResult()
        }
      }
      pom {
        name.set("libgdxpluginannotations")
        description.set("Annotations for use with LibGDXPlugin for IntelliJ")
        url.set("https://github.com/BlueBoxWare/LibGDXPlugin")
        licenses {
          license {
            name.set("The Apache License, Version 2.0")
            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
          }
        }
        developers {
          developer {
            id.set("BlueBoxWare")
          }
        }
      }
    }
  }

}
