dependencies {
    compileOnly(project(":shared"))
    compileOnly(project(":api"))
    compileOnly("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
}

tasks.jar {
    archiveBaseName.set("velocity")
}