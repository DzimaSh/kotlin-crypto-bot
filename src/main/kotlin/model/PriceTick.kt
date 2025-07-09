package by.dzimash.model

import java.math.BigDecimal

/**
 * A generic representation of a price update from any exchange.
 * This is the common language our entire application will speak.
 *
 * @param exchange The name of the exchange (e.g., "Binance").
 * @param symbol The trading pair (e.g., "SHIBUSDT").
 * @param price The latest price, using BigDecimal for precision.
 * @param timestamp The time the tick was received, in milliseconds.
 */
data class PriceTick(
    val exchange: String,
    val symbol: String,
    val price: BigDecimal,
    val timestamp: Long = System.currentTimeMillis()
)
