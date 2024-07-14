dependencies {
    compileOnly(project(":shared"))
    compileOnly(project(":api"))
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
}

tasks.jar {
    archiveBaseName.set("paper")
}