@file: Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Kotlin.version
    `maven-publish`
    signing
}

group = "com.hiczp"
version = "1.0.0"
description = "Retrofit inspired Http client base on CIO"

repositories {
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/kotlinx/")
}

//kotlin
dependencies {
    api(kotlin("stdlib-jdk8"))
    api(kotlin("reflect"))
}
tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = Java.version
        freeCompilerArgs = listOf(
            "-Xuse-experimental=kotlin.Experimental",
            "-Xjvm-default=enable"
        )
    }
}

//cio
dependencies {
    // https://mvnrepository.com/artifact/io.ktor/ktor-client-cio
    api("io.ktor:ktor-client-cio:${Ktor.version}")
}

//test
dependencies {
    // https://mvnrepository.com/artifact/io.ktor/ktor-client-gson
    testImplementation("io.ktor:ktor-client-gson:${Ktor.version}")
    // https://mvnrepository.com/artifact/com.github.salomonbrys.kotson/kotson
    testImplementation("com.github.salomonbrys.kotson:kotson:2.5.0")
    // https://mvnrepository.com/artifact/io.ktor/ktor-client-logging-jvm
    testImplementation("io.ktor:ktor-client-logging-jvm:${Ktor.version}")
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    testImplementation("org.slf4j:slf4j-simple:1.7.26")
    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
    // https://mvnrepository.com/artifact/io.ktor/ktor-client-mock-jvm
    testImplementation("io.ktor:ktor-client-mock-jvm:${Ktor.version}")
}

tasks.register<Jar>("sourcesJar") {
    from(sourceSets.main.get().allSource)
    archiveClassifier.set("sources")
}

tasks.register<Jar>("javadocJar") {
    from(tasks.javadoc)
    archiveClassifier.set("javadoc")
}

publishing {
    repositories {
        maven {
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = project.properties["ossUsername"].toString()
                password = project.properties["ossPassword"].toString()
            }
        }
    }

    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            afterEvaluate {
                artifactId = tasks.jar.get().archiveBaseName.get()
            }

            pom {
                name.set(project.name)
                description.set(project.description)
                url.set("https://github.com/czp3009/caeruleum")

                licenses {
                    license {
                        name.set("Apache License Version 2.0")
                        url.set("http://www.apache.org/licenses/")
                    }
                }

                developers {
                    developer {
                        id.set("czp3009")
                        name.set("czp3009")
                        email.set("czp3009@gmail.com")
                        url.set("https://www.hiczp.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/czp3009/caeruleum")
                    developerConnection.set("scm:git:ssh://github.com/czp3009/caeruleum")
                    url.set("https://github.com/czp3009/caeruleum")
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}
