package by.dzimash

import by.dzimash.di.appModule
import by.dzimash.handler.DataHandler
import by.dzimash.utils.logger
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.logger.slf4jLogger

object Main : KoinComponent {
    private val logger by logger()
    private val dataHandler: DataHandler by inject()

    fun run() = runBlocking {
        logger.info("Starting Crypto Bot...")

        try {
            dataHandler.listenToPriceTicks("SHIBUSDT")
                .collect { priceTick ->
                    logger.info("Received Tick: $priceTick")
                }
        } catch (e: Exception) {
            logger.error("A critical error occurred in the main loop", e)
        }

        logger.info("Crypto Bot stopped.")
    }
}

fun main() {
    startKoin {
        slf4jLogger()
        modules(appModule)
    }
    Main.run()
}
