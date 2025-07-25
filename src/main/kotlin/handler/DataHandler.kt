package by.dzimash.handler

import by.dzimash.model.PriceTick
import kotlinx.coroutines.flow.Flow

/**
 * An interface for a component that provides a real-time stream of price data for a given symbol.
 */
interface DataHandler {
    /**
     * Establishes a connection and listens for price updates.
     * The connection is managed within the Flow itself. It starts when the flow is collected
     * and should clean up when the collecting coroutine is cancelled.
     *
     * @param symbol The trading pair to listen to (e.g. "SHIBUSDT").
     * @return A Flow that emits a new [PriceTick] for every price update.
     */
    fun listenToPriceTicks(symbol: String): Flow<PriceTick>
}