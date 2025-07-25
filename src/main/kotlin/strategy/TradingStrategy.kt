package by.dzimash.strategy

import by.dzimash.model.PriceTick
import by.dzimash.model.Signal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * An interface for a trading strategy. It defines the "brain" of the bot.
 */
interface TradingStrategy {
    /**
     * A state that indicates whether the strategy is currently holding an asset.
     * `true` if we have bought and are waiting to sell.
     * `false` if we have sold and are waiting to buy.
     */
    val inPosition: StateFlow<Boolean>

    /**
     * Evaluates a stream of price ticks and generates a stream of trading signals.
     *
     * @param priceTicks The incoming flow of real-time market prices.
     * @return A flow of BUY or SELL signals based on the strategy's logic.
     */
    fun evaluate(priceTicks: Flow<PriceTick>): Flow<Signal>
}