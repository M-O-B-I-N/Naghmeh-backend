
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
}

group = "mobin.shabanifar"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.core.jvm)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.sqlite.jdbc)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.core)
    implementation(libs.gson)
    implementation(libs.server.sessions)
    implementation(libs.ktor.serialization.gson)
    implementation(libs.server.content.negotiation.jvm)
    implementation(libs.apache)
    implementation(libs.translate)

    //Auth
    implementation(libs.h2database)
    implementation(libs.ktor.auth.jwt)
    implementation(libs.ktor.auth)
    implementation(libs.jbcrypt)

    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.ktor.server.test.host)
}
