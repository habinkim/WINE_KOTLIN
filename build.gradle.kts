import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    kotlin("plugin.jpa") version "1.8.22"
    kotlin("kapt") version "1.7.10"
}

group = "io.directional"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

val queryDslVersion = "5.0.0:jakarta"
val mapstructVersion = "1.5.5.Final"

val asciidoctorExt: Configuration by configurations.creating
val snippetsDir by extra { file("build/generated-snippets") }

dependencies {
    // spring
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // database
    compileOnly("com.h2database:h2")
    testRuntimeOnly("com.h2database:h2")

    // persistence
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.querydsl:querydsl-jpa:${queryDslVersion}")
    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0")

    // boiler plate
    implementation("org.mapstruct:mapstruct:${mapstructVersion}")
    implementation("org.mapstruct:mapstruct-processor:${mapstructVersion}")

    // opencsv
    implementation("com.opencsv:opencsv:5.7.1")

    // kapt
    kapt("org.mapstruct:mapstruct-processor:${mapstructVersion}")
    kapt("com.querydsl:querydsl-apt:${queryDslVersion}")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")

    // kaptTest
    kaptTest("org.mapstruct:mapstruct-processor:${mapstructVersion}")
    kaptTest("com.querydsl:querydsl-apt:${queryDslVersion}")
    kaptTest("org.springframework.boot:spring-boot-configuration-processor")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    outputs.dir(snippetsDir)
}


tasks.asciidoctor {
    inputs.dir(snippetsDir)
    configurations(asciidoctorExt.name)
    dependsOn(tasks.test)

    doFirst {
        delete {
            file("src/main/resources/static/docs")
        }
    }

    doLast {
        copy {
            from(file("build/docs/asciidoc"))
            into(file("src/main/resources/static/docs"))
        }
    }
}

tasks.build {
    dependsOn(tasks.asciidoctor)
}

tasks.bootJar {
    dependsOn(tasks.asciidoctor)
}
