package by.dzimash.config

/**
 * A type-safe representation of a single data handler block from the application.conf file.
 */
data class DataHandlerConfig(
    val enabled: Boolean,
    val type: String,
    val parameters: Map<String, String>
) {
    fun getString(key: String): String {
        return parameters[key] ?: throw IllegalArgumentException("Missing parameter '$key' for data handler type $type")
    }
}