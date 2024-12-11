plugins {
    `maven-publish`
    id("io.papermc.paperweight.userdev") version "1.7.7"
}

repositories {
    maven { url = uri("https://repo.codemc.io/repository/maven-releases/") }

}

dependencies {
    implementation(project(":api"))
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")

    compileOnly("com.github.retrooper:packetevents-spigot:2.4.0")
}

tasks.processResources {
    val props = mapOf("version" to rootProject.version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks.jar {
    archiveFileName.set("api-paper.jar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

tasks.assemble {
    dependsOn(tasks.reobfJar)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set("DreamMCAPI-Paper")
                description.set("Paper version of API for DreamMC server")
                url.set("https://nexus.dreammc.pl/")
                scm {
                    url.set("https://github.com/DreamMC-Network/dreammc-api")
                }
            }
        }
    }

    repositories {
        maven {
            name = "Nexus"
            url = uri(
                "https://nexus.dreammc.pl/repository/maven-" + (project.findProperty("repo") as String? ?: "") + "/"
            )
            credentials {
                username = "admin"
                password = project.findProperty("nexusPassword") as String? ?: ""
            }
        }
    }
}