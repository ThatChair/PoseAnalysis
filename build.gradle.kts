import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  java
  application
  kotlin("jvm") version "1.9.0"
  id("com.github.gmazzo.buildconfig") version "3.0.0"
  id("org.openjfx.javafxplugin") version "0.0.13"
}
val mainClass = "com.pose.analysis.Main"

buildConfig {
  buildConfigField ("long", "BUILD_TIME", "${System.currentTimeMillis()}L")
}

tasks {
  register("fatJar", Jar::class.java) {
    archiveClassifier.set("all")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
      attributes("Main-Class" to mainClass)
    }
    from(configurations.runtimeClasspath.get()
      .onEach { println("add from dependencies: ${it.name}") }
      .map { if (it.isDirectory) it else zipTree(it) })
    val sourcesMain = sourceSets.main.get()
    sourcesMain.allSource.forEach { println("add from sources: ${it.name}") }
    from(sourcesMain.output)
  }
}

repositories {
  mavenCentral()
  google()
  maven { setUrl("https://plugins.gradle.org/m2/") }
}

javafx {
  version = "19"
  modules = "javafx.controls,javafx.graphics,javafx.base".split(",").toMutableList()
}
application {
  mainClass = "com.pose.analysis.Main"
}



dependencies {
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
  implementation("org.jetbrains.kotlin:kotlin-stdlib")
  implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.jetbrains.kotlinx:multik-core:0.2.2")
    implementation("org.jetbrains.kotlinx:multik-default:0.2.2")
}

java {
  withSourcesJar()
  withJavadocJar()
}

configure<JavaPluginConvention> {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
}

tasks.register<Copy>("copyJars") {
  from(configurations.runtimeClasspath)
  into("${buildDir}/libs")
}

tasks.register<Exec>("jpackage") {
  dependsOn("copyJars")

  doLast {
    exec {
      commandLine(
        "jpackage",
        "--input", "${project.buildDir}/jlink",
        "--main-jar", "${project.buildDir}/libs/${project.name}-${project.version}.jar",
        "--output", "${project.buildDir}/jpackage",
        "--name", "YourApp",
        "--main-class", mainClass,
        "--type", "exe",
        "--win-shortcut"
      )
    }
  }
}



