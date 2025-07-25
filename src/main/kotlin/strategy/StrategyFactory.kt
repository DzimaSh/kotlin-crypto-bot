package by.dzimash.strategy

import by.dzimash.config.StrategyConfig
import by.dzimash.strategy.impl.RangeStrategy

/**
 * A factory for creating TradingStrategy instances from a configuration object.
 */
class StrategyFactory {
    fun create(config: StrategyConfig): TradingStrategy {
        return when (config.type.lowercase()) {
            "range" -> RangeStrategy(
                symbol = config.symbol,
                lowerBound = config.getBigDecimal("lower-bound"),
                upperBound = config.getBigDecimal("upper-bound")
            )
            else -> throw IllegalArgumentException("Unknown strategy type: ${config.type}")
        }
    }
}