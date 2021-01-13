package org.ddnss.xecortex.corex.util

class InvalidConfigException(name: String) : Exception("Config property \"$name\" is invalid or null")