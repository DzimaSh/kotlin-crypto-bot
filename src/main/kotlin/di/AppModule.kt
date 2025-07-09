package by.dzimash.di

import by.dzimash.handler.DataHandler
import by.dzimash.handler.impl.BinanceDataHandler
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

// Central configuration module
val appModule = module {

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

    single<DataHandler> {
        BinanceDataHandler(get(), get())
    }
}