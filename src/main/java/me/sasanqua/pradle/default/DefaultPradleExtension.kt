package me.sasanqua.pradle.default

import me.sasanqua.pradle.PradleExtension
import me.sasanqua.pradle.PradleSourceSetContainer
import org.gradle.api.Action

class DefaultPradleExtension(override val sourceSets: PradleSourceSetContainer) : PradleExtension {
    override fun sourceSets(action: Action<PradleSourceSetContainer>) = action.execute(sourceSets)
}