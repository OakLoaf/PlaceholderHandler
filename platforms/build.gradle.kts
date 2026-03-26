publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.group.toString() + ".placeholderhandler"
            artifactId = rootProject.name + "-" + project.name
            version = rootProject.version.toString()
            from(project.components["java"])
        }
    }
}