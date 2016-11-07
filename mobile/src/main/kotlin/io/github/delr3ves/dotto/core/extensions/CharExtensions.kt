package io.github.delr3ves.dotto.core.extensions

import org.apache.commons.lang3.StringUtils

object CharExtensions {

    fun Char.normalize(): Char {
        return StringUtils.stripAccents(this.toString()).toCharArray().first().toLowerCase()
    }
}