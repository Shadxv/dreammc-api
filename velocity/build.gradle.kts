plugins {
    `maven-publish`
}

dependencies {
    implementation(project(":api"))
    compileOnly("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
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
                name.set("SProjectAPI-Velocity")
                description.set("Velocity version of API for SProject server")
                url.set("https://nexus.senceye.com/")
                scm {
                    url.set("https://github.com/Secret-Project-Server/sproject-api")
                }
            }
        }
    }

    repositories {
        maven {
            name = "Nexus"
            url = uri(
                "https://nexus.senceye.com/repository/maven-" + (project.findProperty("repo") as String? ?: "") + "/"
            )
            credentials {
                username = "admin"
                password = project.findProperty("nexusPassword") as String? ?: ""
            }
        }
    }
}