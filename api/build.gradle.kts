dependencies {
    compileOnly(project(":shared"))

    implementation("org.mongodb:mongodb-driver-sync:5.1.2")
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}