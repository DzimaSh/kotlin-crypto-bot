package by.dzimash.di

import by.dzimash.config.ConfigService
import by.dzimash.engine.StrategyEngine
import by.dzimash.handler.DataHandler
import by.dzimash.handler.DataHandlerFactory
import by.dzimash.strategy.StrategyFactory
import by.dzimash.strategy.TradingStrategy
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

// Central configuration module
val appModule = module {

    // --- SHARED COMPONENTS ---
    single<Json> {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }
    }

    single<HttpClient> {
        HttpClient(CIO) {
            install(WebSockets)
            install(ContentNegotiation) {
                json(get())
            }
        }
    }

    // --- CONFIGURATION ---
    single { ConfigService() }

    // --- FACTORIES & HANDLERS ---
    single { DataHandlerFactory(get(), get()) }
    
    single<DataHandler> { 
        val configService = get<ConfigService>()
        val dataHandlerFactory = get<DataHandlerFactory>()
        
        val dataHandlerConfigs = configService.loadDataHandlers()
        val enabledConfig = dataHandlerConfigs.firstOrNull { it.enabled }
            ?: throw IllegalStateException("No enabled data handlers found in configuration")
        
        dataHandlerFactory.create(enabledConfig)
    }
    
    single { StrategyFactory() }

    // --- ENGINE ---
    factory { (strategy: TradingStrategy) ->
        StrategyEngine(get(), strategy)
    }
}