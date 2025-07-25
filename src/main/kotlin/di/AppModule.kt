package by.dzimash.di

import by.dzimash.engine.StrategyEngine
import by.dzimash.handler.DataHandler
import by.dzimash.handler.impl.BinanceDataHandler
import by.dzimash.strategy.TradingStrategy
import by.dzimash.strategy.impl.RangeStrategy
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import java.math.BigDecimal

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

    // --- DATA HANDLER ---
    single<DataHandler> {
        BinanceDataHandler(get(), get())
    }

    // --- STRATEGY ---
    factory<TradingStrategy> { (symbol: String, lower: BigDecimal, upper: BigDecimal) ->
        RangeStrategy(symbol, lower, upper)
    }

    // --- ENGINE ---
    factory<StrategyEngine> { (strategy: TradingStrategy) ->
        StrategyEngine(get(), strategy)
    }
}