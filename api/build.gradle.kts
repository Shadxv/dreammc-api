dependencies {
    implementation(project(":shared"))

    implementation("org.mongodb:mongodb-driver-sync:5.1.2")
}

tasks.jar {
    archiveFileName.set("api.jar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}