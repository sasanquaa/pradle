package me.sasanqua.pradle.environment

import java.io.File

object PythonExecutableFinder {
    private const val PATH = "PATH"
    private val OS_NAME = System.getProperty("os.name")
    private val EXECUTABLE = if (OS_NAME.contains("windows", true)) {
        "python.exe"
    } else {
        "python"
    }

    fun find(): File? {
        val directories = findDirectoriesFromPath()
        for (dir in directories) {
            val file = File(dir, EXECUTABLE)
            if (file.isFile) {
                return file
            }
        }
        return null
    }

    private fun findDirectoriesFromPath(): List<File> {
        val path = System.getenv().firstNotNullOfOrNull { (name, paths) ->
            if (name.equals(PATH, true)) {
                return@firstNotNullOfOrNull paths
            }
            null
        }.orEmpty()
        return path.split(File.pathSeparator).map(::File)
    }
}