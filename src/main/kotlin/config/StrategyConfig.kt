package by.dzimash.config

import java.math.BigDecimal

/**
 * A type-safe representation of a single strategy block from the application.conf file.
 */
data class StrategyConfig(
    val enabled: Boolean,
    val symbol: String,
    val type: String,
    val parameters: Map<String, String>
) {
    fun getBigDecimal(key: String): BigDecimal {
        val value = parameters[key] ?: throw IllegalArgumentException("Missing parameter '$key' for symbol $symbol")
        return BigDecimal(value)
    }
}