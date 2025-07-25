package by.dzimash

import by.dzimash.config.ConfigService
import by.dzimash.di.appModule
import by.dzimash.engine.StrategyEngine
import by.dzimash.strategy.StrategyFactory
import by.dzimash.strategy.TradingStrategy
import by.dzimash.utils.logger
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import org.koin.logger.slf4jLogger
import java.math.BigDecimal

object Main : KoinComponent {
    private val logger by logger()

    private val configService: ConfigService by inject()
    private val strategyFactory: StrategyFactory by inject()

    fun run() = runBlocking {
        logger.info("Starting Crypto Bot...")

        val strategyConfigs = configService.loadStrategies()

        coroutineScope {
            strategyConfigs
                .filter { it.enabled } // Only run enabled strategies
                .forEach { config ->
                    launch {
                        logger.info("Launching strategy for ${config.symbol}...")
                        val strategy = strategyFactory.create(config)

                        val engine: StrategyEngine = get { parametersOf(strategy) }

                        engine.start(config.symbol)
                            .catch { e -> logger.error("Error in strategy for ${config.symbol}", e) }
                            .collect { signal ->
                                logger.warn(">>>>>> SIGNAL [${config.symbol}]: $signal <<<<<<")
                            }
                    }
                }
        }
        logger.info("All strategies have been stopped. Shutting down.")
    }
}

fun main() {
    startKoin {
        slf4jLogger()
        modules(appModule)
    }
    Main.run()
}
