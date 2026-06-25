plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("com.gradleup.shadow") version "8.3.5"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")

    implementation(platform("org.mongodb:mongodb-driver-bom:5.8.0"))
    implementation("org.mongodb:mongodb-driver-sync")
    implementation("org.spongepowered:configurate-yaml:4.2.0")

    compileOnly("com.comphenix.protocol:ProtocolLib:5.3.0")

    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("com.mysql:mysql-connector-j:9.3.0")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks {
    processResources {
        val props = mapOf("version" to version)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    shadowJar {
        archiveClassifier.set("")
        mergeServiceFiles()
    }

    jar {
        enabled = false
    }

    build {
        dependsOn(shadowJar)
        finalizedBy("uploadJar")
    }

    register<Exec>("uploadJar") {
        dependsOn(shadowJar)

        commandLine(
            "scp",
            "${layout.buildDirectory.get()}/libs/goliath-paper-core-1.0.jar",
            "server@mcserver.ch:/data/download/goliath-paper-core-1.0.jar"
        )
    }

    runServer {
        minecraftVersion("1.21.11")
        jvmArgs("-Xms2G", "-Xmx2G")
    }
}