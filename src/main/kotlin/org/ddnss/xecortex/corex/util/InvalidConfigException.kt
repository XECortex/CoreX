package org.ddnss.xecortex.corex.util

class InvalidConfigException(private val name: String) : Exception("Config property \"$name\" is invalid or null")