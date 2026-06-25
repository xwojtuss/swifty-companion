plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
}

group = "edu.swifty.companion"
version = "1.0.0"
application {
    mainClass = "edu.swifty.companion.ApplicationKt"
}

dependencies {
    api(projects.core)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation("io.ktor:ktor-server-cors:3.5.0")
    implementation("io.ktor:ktor-server-auth:3.5.0")
    implementation("io.ktor:ktor-server-sessions:3.5.0")
    implementation("io.ktor:ktor-client-cio:3.5.0")
    implementation("io.ktor:ktor-server-content-negotiation:3.5.0")
    implementation(libs.ktor.serializationKotlinxJson)
    implementation(libs.kotlinx.serializationJson)
    implementation(libs.ktor.clientCore)
    implementation(libs.ktor.clientContentNegotiation)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
}