package me.sasanqua.pradle.dependencies

import org.gradle.api.file.SourceDirectorySet
import java.nio.file.Path
import kotlin.io.path.Path

interface PradleSourceSet {
    val python: SourceDirectorySet
    val resources: SourceDirectorySet

    companion object {
        val DEFAULT_PYTHON_MAIN_PATH: Path = Path("src").resolve("main").resolve("python")
        val DEFAULT_PYTHON_TEST_PATH: Path = Path("src").resolve("test").resolve("python")
        val DEFAULT_RESOURCES_MAIN_PATH: Path = Path("src").resolve("main").resolve("resources")
        val DEFAULT_RESOURCES_TEST_PATH: Path = Path("src").resolve("test").resolve("resources")
    }
}