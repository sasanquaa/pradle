plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        register("pradle") {
            id = "pradle"
            implementationClass = "me.sasanqua.pradle.PradlePlugin"
        }
    }
}

fun createService(service: File, interfaceClass: String, implementationClass: String) {
    val extension = service.resolve(interfaceClass)
    extension.createNewFile()
    extension.writeText(implementationClass)
}