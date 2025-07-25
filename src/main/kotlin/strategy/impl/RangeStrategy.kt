package by.dzimash.strategy.impl

import by.dzimash.model.PriceTick
import by.dzimash.model.Signal
import by.dzimash.model.SignalType
import by.dzimash.strategy.TradingStrategy
import by.dzimash.utils.logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull
import java.math.BigDecimal

/**
 * A simple range-bound trading strategy.
 * It buys when the price drops to or below a lower bound
 * and sells when the price rises to or above an upper bound.
 *
 * @param symbol The symbol this strategy applies to.
 * @param lowerBound The price at which to generate a BUY signal.
 * @param upperBound The price at which to generate a SELL signal.
 */
class RangeStrategy(
    private val symbol: String,
    private val lowerBound: BigDecimal,
    private val upperBound: BigDecimal
) : TradingStrategy {

    private val logger by logger()

    // We use a MutableStateFlow to keep track of our position state.
    // It starts as `false` (we don't have any assets yet).
    private val _inPosition = MutableStateFlow(false)
    override val inPosition: StateFlow<Boolean> = _inPosition.asStateFlow()

    init {
        // Basic validation
        require(lowerBound < upperBound) { "Lower bound must be less than upper bound." }
        logger.info("RangeStrategy initialized for $symbol: [Buy <= $lowerBound, Sell >= $upperBound]")
    }

    override fun evaluate(priceTicks: Flow<PriceTick>): Flow<Signal> = priceTicks
        .filter { it.symbol == this.symbol } // Only process ticks for our symbol
        .mapNotNull { tick ->
            val currentPrice = tick.price
            val currentlyInPosition = inPosition.value

            // Decision logic
            when {
                !currentlyInPosition && currentPrice <= lowerBound -> {
                    _inPosition.value = true // Update state: we are now in a position
                    logger.info("BUY signal triggered at price $currentPrice (<= $lowerBound)")
                    Signal(symbol, SignalType.BUY, currentPrice)
                }

                currentlyInPosition && currentPrice >= upperBound -> {
                    _inPosition.value = false // Update state: we have sold
                    logger.info("SELL signal triggered at price $currentPrice (>= $upperBound)")
                    Signal(symbol, SignalType.SELL, currentPrice)
                }

                else -> null
            }
        }
}