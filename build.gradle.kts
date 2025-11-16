import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("maven-publish")
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("com.github.blueboxware.tocme") version "1.8"
    id("org.jetbrains.intellij.platform") version "2.10.4"
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
        jetbrainsRuntime()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity(providers.gradleProperty("platformVersion")) {
            useInstaller = false
        }
        bundledPlugins(providers.gradleProperty("platformBundledPlugins").map { it.split(',') })
        bundledModules(providers.gradleProperty("platformBundledModules").map { it.split(',') })
        jetbrainsRuntime()
        pluginVerifier()
        testFramework(TestFrameworkType.Platform)
        testFramework(TestFrameworkType.Plugin.Java)
    }
    testImplementation("junit:junit:4.13.2")
}

kotlin {
    jvmToolchain(21)
}

intellijPlatform {
    pluginConfiguration {
        name = providers.gradleProperty("pluginName")
        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
            untilBuild = provider { null }
        }
    }
    pluginVerification {
        ides {
//            ide(IntelliJPlatformType.IntellijIdeaCommunity, providers.gradleProperty("platformVersion").get())
            create(IntelliJPlatformType.IntellijIdeaCommunity, "253-EAP-SNAPSHOT")
            recommended()
        }
    }
}

sourceSets {
    main {
        java.srcDirs("gen")
        java.srcDirs("src/main/kotlin")
        java.exclude("com/gmail/blueboxware/libgdxplugin/annotations/**")
        resources.srcDirs("resources")
    }

    create("annotations") {
        java {
            srcDir("src/main/java")
        }
    }

}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }

    named<JavaCompile>("compileAnnotationsJava") {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    named<KotlinCompile>("compileTestKotlin") {
        compilerOptions {
            optIn.set(listOf("org.jetbrains.kotlin.analysis.api.permissions.KaAllowProhibitedAnalyzeFromWriteAction"))
        }
    }

    runIde {
        maxHeapSize = "8g"
        systemProperties = mapOf(
            "idea.ProcessCanceledException" to "disabled",
            "idea.is.internal" to "true",
            "idea.kotlin.plugin.use.k2" to "true"
        )
    }

    tocme {
        doc("README.md")
    }

    buildSearchableOptions {
        enabled = true
    }

    test {
        systemProperty("idea.home.path", System.getenv("LIBGDXPLUGIN_IDEA"))
        environment("NO_FS_ROOTS_ACCESS_CHECK", "1")
        isScanForTestClasses = false
        include("**/Test*.class")
        include("com/gmail/blueboxware/libgdxplugin/ShowInfo.class")
        exclude("**/*$*.class")
        jvmArgumentProviders += CommandLineArgumentProvider {
            listOf("-Didea.kotlin.plugin.use.k2=true")
        }
    }

    register<Jar>("annotationsJar") {
        archiveBaseName.set("libgdxpluginannotations")
        from(sourceSets.getByName("annotations").output)
        include("com/gmail/blueboxware/libgdxplugin/annotations/**")
        archiveVersion.set(providers.gradleProperty("pluginVersion"))
    }

    register<Jar>("annotationsSourcesJar") {
        archiveClassifier.set("sources")
        from(sourceSets.getByName("annotations").allSource)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        include(project.tasks.getByName<Jar>("annotationsJar").includes)
        archiveBaseName.set(project.tasks.getByName<Jar>("annotationsJar").archiveBaseName)
        archiveVersion.set(providers.gradleProperty("pluginVersion"))
    }

    fun generateParserTask(path: String, lexer: String, parser: String) {
        val name = path.uppercaseFirstChar()
        val bnfTask = register<GenerateParserTask>("generate${name}Parser") {
            sourceFile = file("src/main/kotlin/com/gmail/blueboxware/libgdxplugin/filetypes/$path/$parser.bnf")
            targetRootOutputDir = file("gen")
            pathToParser = "gen/com/gmail/blueboxware/libgdxplugin/filetypes/$path"
            pathToPsiRoot = "gen/com/gmail/blueboxware/libgdxplugin/filetypes/$path/psi"
        }
        val lexerTask = register<GenerateLexerTask>("generate${name}Lexer") {
            sourceFile = file("src/main/kotlin/com/gmail/blueboxware/libgdxplugin/filetypes/$path/$lexer.flex")
            targetOutputDir = file("gen/com/gmail/blueboxware/libgdxplugin/filetypes/$path")
            dependsOn(bnfTask)
        }
        withType<KotlinCompile>() {
            dependsOn(lexerTask)
        }
    }

    generateParserTask("tree", "Tree", "Tree")
    generateParserTask("atlas2", "Atlas2Lexer", "LibGDXAtlas2")
    generateParserTask("bitmapFont", "_BitmapFontLexer", "BitmapFont")
    generateParserTask("json", "_GdxJsonLexer", "GdxJson")
    generateParserTask("skin", "_SkinLexer", "LibGDXSkin")

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
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
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
