package me.sasanqua.pradle.internal

import me.sasanqua.pradle.dependencies.PradleSourceSet
import org.gradle.api.file.SourceDirectorySet

data class DefaultPradleSourceSet(
    override val name: String,
    override val python: SourceDirectorySet,
    override val resources: SourceDirectorySet
) : PradleSourceSet