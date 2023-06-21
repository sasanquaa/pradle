package me.sasanqua.pradle.tasks.utils

fun venvCommand(executable: String, vararg args: String): List<String> = listOf(executable, "-m", "venv", *args)

fun checkVenvCommand(executable: String): List<String> = listOf(executable, "-c", "\"import venv\"")

fun pipFreezeCommand(executable: String): List<String> = listOf(executable, "-m", "pip", "freeze")

fun pipInstallRequirementsCommand(executable: String, requirementsPath: String): List<String> =
    listOf(executable, "-m", "pip", "install", "-q", "-r", requirementsPath)

fun pipUninstallRequirementsCommand(executable: String, requirementsPath: String): List<String> =
    listOf(executable, "-m", "pip", "uninstall", "-q", "-y", "-r", requirementsPath)
