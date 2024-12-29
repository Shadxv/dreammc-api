plugins {
    `maven-publish`
}

dependencies {
    implementation(project(":api"))
    compileOnly("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
    implementation("org.yaml:snakeyaml:2.0")
}

tasks.processResources {
    val props = mapOf("version" to project.version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("velocity-plugin.json") {
        expand(props)
    }
}

tasks.jar {
    archiveFileName.set("api-velocity.jar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set("DreamMCAPI-Velocity")
                description.set("Velocity version of API for DreamMC server")
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