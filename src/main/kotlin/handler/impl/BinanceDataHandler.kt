package by.dzimash.handler.impl

import by.dzimash.handler.DataHandler
import by.dzimash.model.PriceTick
import by.dzimash.model.binance.Ticker
import by.dzimash.utils.logger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.json.Json
import java.math.BigDecimal

class BinanceDataHandler(
    private val client: HttpClient,
    private val json: Json,
    private val baseUrl: String = "wss://stream.binance.com:9443/ws"
) : DataHandler {

    private val logger by logger()

    override fun listenToPriceTicks(symbol: String): Flow<PriceTick> = flow {
        val lowerCaseSymbol = symbol.lowercase()

        try {
            client.webSocket("${baseUrl}/${lowerCaseSymbol}@ticker") {
                logger.info("Connected to Binance WebSocket for symbol: $symbol")

                incoming.consumeAsFlow()
                    .mapNotNull { frame ->
                        if (frame is Frame.Text) {
                            try {
                                json.decodeFromString<Ticker>(frame.readText())
                            } catch (e: Exception) {
                                logger.error("Error while parsing ${frame.readText()}: ${e.message}")
                                null
                            }
                        }
                        else {
                            null
                        }
                    }
                    .collect { ticker ->
                        val priceTick = PriceTick(
                            exchange = "Binance",
                            symbol = ticker.symbol,
                            price = BigDecimal(ticker.price)
                        )

                        emit(priceTick)
                    }
            }
        } catch (e: Exception) {
            logger.error("Error with Binance WebSocket connection", e)
        } finally {
            logger.info("Disconnected from Binance WebSocket for symbol: $symbol")
        }
    }
}