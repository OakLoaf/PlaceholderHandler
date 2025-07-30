plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version("8.3.0")
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "com.gradleup.shadow")

    group = "org.lushplugins"
    version = "1.0.0-alpha6"

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://repo.papermc.io/repository/maven-public/") // Paper
        maven("https://repo.helpch.at/releases/") // PlaceholderAPI
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        build {
            dependsOn(shadowJar)
        }

        shadowJar {
            minimize()

            archiveFileName.set("${project.name}-${project.version}.jar")
        }
    }
}

subprojects {
    apply(plugin = "maven-publish")

    dependencies {
        compileOnly("org.jetbrains:annotations:26.0.1")
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))

        registerFeature("optional") {
            usingSourceSet(sourceSets["main"])
        }

        withSourcesJar()
    }

    publishing {
        repositories {
            maven {
                name = "lushReleases"
                url = uri("https://repo.lushplugins.org/releases")
                credentials(PasswordCredentials::class)
                authentication {
                    isAllowInsecureProtocol = true
                    create<BasicAuthentication>("basic")
                }
            }

            maven {
                name = "lushSnapshots"
                url = uri("https://repo.lushplugins.org/snapshots")
                credentials(PasswordCredentials::class)
                authentication {
                    isAllowInsecureProtocol = true
                    create<BasicAuthentication>("basic")
                }
            }
        }

        publications {
            create<MavenPublication>("maven") {
                groupId = rootProject.group.toString() + ".placeholderhandler"
                artifactId = rootProject.name + "-" + project.name
                version = rootProject.version.toString()
                from(project.components["java"])
            }
        }
    }
}
