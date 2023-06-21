plugins {
    id("pradle")
}

pradle {
    dependencies {
        pip("numpy")
        pip("scipy")
        pip("pandas")
    }
}
