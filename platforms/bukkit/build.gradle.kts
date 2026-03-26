plugins {
    id("xyz.jpenilla.run-paper") version("2.2.4")
}

dependencies {
    // Dependencies
    api(project(":common"))
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")

    // Soft Dependencies
    compileOnly("me.clip:placeholderapi:2.12.2")
}