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