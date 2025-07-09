package by.dzimash.model.binance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ticker(
    @SerialName("s") val symbol: String, // "s" is the symbol in the JSON
    @SerialName("p") val price: String,  // "p" is the price in the JSON
)
