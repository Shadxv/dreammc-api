dependencies {
    implementation(project(":shared"))
    implementation("org.mongodb:mongodb-driver-sync:5.1.2")
    implementation("net.kyori:adventure-text-serializer-plain:4.17.0")
    implementation("com.google.guava:guava:33.3.1-jre")
}

tasks.jar {
    archiveFileName.set("api.jar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}