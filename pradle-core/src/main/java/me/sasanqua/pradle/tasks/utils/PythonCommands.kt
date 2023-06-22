package me.sasanqua.pradle.tasks.utils

private const val PACKAGE_CHECK_SCRIPT: String = "\"import %s\""
private const val VERSION_CHECK_SCRIPT: String =
    "\"import sys;print(str(sys.version_info.major) + '.' + str(sys.version_info.minor) + '.' + str(sys.version_info.micro))\""

const val VENV = "venv"
const val ZIP_APP = "zipapp"

fun pythonCommand(executable: String, vararg args: String): List<String> = listOf(executable, *args)

fun venvCommand(executable: String, vararg args: String): List<String> = pythonCommand(executable, "-m", VENV, *args)

fun zipAppCommand(executable: String, vararg args: String): List<String> =
    pythonCommand(executable, "-m", ZIP_APP, *args)

fun pipInstallRequirementsCommand(executable: String, requirementsPath: String): List<String> =
    listOf(executable, "-m", "pip", "install", "-q", "-r", requirementsPath)


fun pipUninstallRequirementsCommand(executable: String, requirementsPath: String): List<String> =
    listOf(executable, "-m", "pip", "uninstall", "-q", "-y", "-r", requirementsPath)

fun getPythonVersionCommand(executable: String): List<String> = listOf(
    executable,
    "-c",
    VERSION_CHECK_SCRIPT
)

fun checkPackageCommand(executable: String, packageName: String): List<String> =
    listOf(executable, "-c", String.format(PACKAGE_CHECK_SCRIPT, packageName))
