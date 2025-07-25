package by.dzimash.di

import by.dzimash.config.ConfigService
import by.dzimash.engine.StrategyEngine
import by.dzimash.handler.DataHandler
import by.dzimash.handler.impl.BinanceDataHandler
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
    single<DataHandler> { BinanceDataHandler(get(), get()) }
    single { StrategyFactory() }

    // --- ENGINE ---
    factory { (strategy: TradingStrategy) ->
        StrategyEngine(get(), strategy)
    }
}