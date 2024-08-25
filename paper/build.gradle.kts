plugins {
    `maven-publish`
}

dependencies {
    implementation(project(":api"))
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
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

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set("SProjectAPI-Paper")
                description.set("Paper version of API for SProject server")
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