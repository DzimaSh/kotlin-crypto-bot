package by.dzimash.config

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigObject
import by.dzimash.utils.logger

/**
 * Service responsible for loading and parsing the application.conf file.
 */
class ConfigService {
    private val logger by logger()

    private val config = ConfigFactory.load()

    fun loadStrategies(): List<StrategyConfig> {
        logger.info("Loading strategies from configuration...")
        val strategyConfigs = config.getConfigList("bot.strategies")
        return strategyConfigs.map { strategyConfig ->
            val paramsObject = strategyConfig.getObject("parameters")
            val paramsMap = paramsObject.unwrapped().mapValues { it.value.toString() }

            StrategyConfig(
                enabled = strategyConfig.getBoolean("enabled"),
                symbol = strategyConfig.getString("symbol"),
                type = strategyConfig.getString("type"),
                parameters = paramsMap
            )
        }.also {
            logger.info("Found ${it.size} strategies, ${it.count { s -> s.enabled }} are enabled.")
        }
    }

    fun loadDataHandlers(): List<DataHandlerConfig> {
        logger.info("Loading data handlers from configuration...")
        val dataHandlerConfigs = config.getConfigList("bot.data-handlers")
        return dataHandlerConfigs.map { dataHandlerConfig ->
            val paramsMap = if (dataHandlerConfig.hasPath("parameters")) {
                dataHandlerConfig.getObject("parameters").unwrapped().mapValues { it.value.toString() }
            } else {
                emptyMap()
            }

            DataHandlerConfig(
                enabled = dataHandlerConfig.getBoolean("enabled"),
                type = dataHandlerConfig.getString("type"),
                parameters = paramsMap
            )
        }.also {
            logger.info("Found ${it.size} data handlers, ${it.count { h -> h.enabled }} are enabled.")
        }
    }
}