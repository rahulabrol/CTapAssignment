pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        val githubProperties = java.util.Properties()
        if (file("github.properties").exists()) {
            try {
                githubProperties.load(java.io.FileInputStream(file("github.properties")))
            } catch (e: Exception) {
                println(e.toString())
            }
        }

        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/rahulabrol/NetworkClient")
            credentials {
                username = githubProperties["gpr.usr"]?.toString() ?: System.getenv("GPR_USER")
                password =
                    githubProperties["gpr.key"]?.toString() ?: System.getenv("GPR_API_KEY")
            }
        }
    }
}

rootProject.name = "CTapAssignment"
include(":app")
 