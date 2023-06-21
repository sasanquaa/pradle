plugins {
    id("pradle")
}

pradle {
    dependencies {
        pip("numpy")
        pip("pywinauto")
    }
}
