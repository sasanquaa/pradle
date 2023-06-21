package me.sasanqua.pradle.tasks.utils

private const val VENV_CHECK_SCRIPT: String = "import venv"
private const val VERSION_CHECK_SCRIPT: String =
    "import sys;print(str(sys.version_info.major) + '.' + str(sys.version_info.minor) + '.' + str(sys.version_info.micro))"

fun venvCommand(executable: String, vararg args: String): List<String> = listOf(executable, "-m", "venv", *args)

fun checkVenvCommand(executable: String): List<String> = listOf(executable, "-c", "\"$VENV_CHECK_SCRIPT\"")

fun pipInstallRequirementsCommand(executable: String, requirementsPath: String): List<String> =
    listOf(executable, "-m", "pip", "install", "-q", "-r", requirementsPath)

fun pipUninstallRequirementsCommand(executable: String, requirementsPath: String): List<String> =
    listOf(executable, "-m", "pip", "uninstall", "-q", "-y", "-r", requirementsPath)

fun getVersionCommand(executable: String): List<String> = listOf(
    executable,
    "-c",
    "\"$VERSION_CHECK_SCRIPT\""
)
