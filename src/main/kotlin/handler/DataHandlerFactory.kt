package by.dzimash.handler

import by.dzimash.config.DataHandlerConfig
import by.dzimash.handler.impl.BinanceDataHandler
import by.dzimash.utils.logger
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

/**
 * A factory for creating DataHandler instances from a configuration object.
 */
class DataHandlerFactory(
    private val httpClient: HttpClient,
    private val json: Json
) {
    private val logger by logger()

    fun create(config: DataHandlerConfig): DataHandler {
        logger.info("Creating data handler of type: ${config.type}")
        
        return when (config.type.lowercase()) {
            "binance" -> {
                val baseUrl = config.getString("base-url")
                BinanceDataHandler(httpClient, json, baseUrl)
            }
            else -> throw IllegalArgumentException("Unknown data handler type: ${config.type}")
        }
    }
}