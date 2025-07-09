package by.dzimash.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class LoggerDelegate : ReadOnlyProperty<Any, Logger> {
    private lateinit var logger: Logger

    override fun getValue(thisRef: Any, property: KProperty<*>): Logger {
        if (!::logger.isInitialized) {
            val javaClass = thisRef.javaClass.enclosingClass ?: thisRef.javaClass
            logger = LoggerFactory.getLogger(javaClass)
        }
        return logger
    }
}

fun logger(): ReadOnlyProperty<Any, Logger> = LoggerDelegate()
