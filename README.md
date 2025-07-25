# Kotlin Crypto Trader Bot 🤖

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.23-7F52FF?style=for-the-badge&logo=kotlin)
![Ktor](https://img.shields.io/badge/Ktor-3.0.0--beta--2-0095D5?style=for-the-badge)
![Gradle](https://img.shields.io/badge/Gradle-8.7-02303A?style=for-the-badge&logo=gradle)
![License](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)

A high-performance, event-driven cryptocurrency trading bot built with a modern Kotlin technology stack. This project serves as a comprehensive, educational guide for developers looking to master asynchronous programming with Coroutines, build resilient systems with Ktor, and apply clean architecture principles in a real-world financial application.

The primary goal is to trade volatile assets like meme-coins by implementing a simple but effective **Grid Trading** strategy, while building a framework robust enough for future expansion.

---

## 🎯 Project Philosophy

This project is built on four key pillars, derived from the requirements of automated trading:

* **Idiomatic Kotlin:** Writing clean, concise, and null-safe Kotlin code that leverages the language's best features.
* **Asynchronous & Non-Blocking:** Using Kotlin Coroutines and Ktor to efficiently handle thousands of concurrent market events without blocking threads, which is essential for low-latency performance.
* **Clean Architecture:** Decoupling components through interfaces and dependency injection (Koin) to create a system that is highly maintainable, testable, and scalable.
* **Real-World Resilience:** Implementing professional-grade logging, configuration, security, and risk management practices from the ground up.

## 🏗️ Architecture: Event-Driven by Design

The bot is built on an **Event-Driven Architecture (EDA)**, the natural paradigm for reacting to real-time market events. Components are decoupled and communicate through an internal event bus, making the system highly responsive and scalable.

| Component           | Responsibility                                                                                                  | Key Technologies          |
| :------------------ | :-------------------------------------------------------------------------------------------------------------- | :------------------------ |
| **`DataHandler`** | Connects to exchange WebSockets, ingests raw market data, and emits standardized `PriceTick` events.            | Ktor Client, WebSockets   |
| **`StrategyEngine`**| Subscribes to `PriceTick` events, applies trading logic (e.g., Grid Strategy), and publishes `Signal` events.     | Kotlin Coroutines, Flow   |
| **`ExecutionHandler`**| Acts on `Signal` events. Translates them into signed REST API calls to place/cancel orders on the exchange.      | Ktor Client, REST         |
| **`PortfolioManager`**| Tracks current positions, calculates Profit & Loss (P&L), and monitors account equity by listening to order fills. | Exposed ORM, SQL          |

This separation of concerns allows for powerful flexibility. For example, the live `DataHandler` can be swapped with a mock version that replays historical data from a file, allowing the `StrategyEngine` to be backtested with no code changes.

## 🛠️ Tech Stack & Key Libraries

* **Language:** [**Kotlin**](https://kotlinlang.org/)
* **Networking:** [**Ktor**](https://ktor.io/) (for WebSockets and REST APIs)
* **Asynchronicity:** [**Kotlinx Coroutines**](https://github.com/Kotlin/kotlinx.coroutines)
* **JSON Parsing:** [**Kotlinx Serialization**](https://github.com/Kotlin/kotlinx.serialization)
* **Dependency Injection:** [**Koin**](https://insert-koin.io/)
* **Database ORM:** [**JetBrains Exposed**](https://github.com/JetBrains/Exposed) (for trade history)
* **Configuration:** [**Jetpack DataStore**](https://developer.android.com/jetpack/androidx/releases/datastore) (for key-value settings)
* **Logging:** [**Logback**](https://logback.qos.ch/) & [**SLF4J**](https://www.slf4j.org/)

## 🚀 Getting Started

Follow these instructions to get the project up and running on your local machine.

### Prerequisites

* **JDK 17** or newer.
* **IntelliJ IDEA** (Community or Ultimate edition) is highly recommended for the best Kotlin development experience.

### Installation & Setup

1.  **Clone the repository:**
    ```sh
    git clone [https://github.com/your-username/kotlin-crypto-bot.git](https://github.com/your-username/kotlin-crypto-bot.git)
    cd kotlin-crypto-bot
    ```

2.  **Open in IntelliJ IDEA:**
    * Open IntelliJ and select `File > Open...`.
    * Navigate to and select the cloned project's `build.gradle` file.
    * Trust the project and let IntelliJ import it. Gradle will automatically download all the required dependencies.

3.  **Run the application:**
    * Navigate to the `src/main/kotlin/main/Main.kt` file.
    * Click the green "play" button next to the `main` function.
    * You should see log output in the "Run" console, indicating a successful connection to the Binance WebSocket and real-time price ticks for the configured symbol.

## ⚙️ Configuration

Configuration is managed through the `application.conf` file located in `src/main/resources/`.

* **Strategies:** Multiple trading strategies can be configured in the `bot.strategies` section:
    ```hocon
    strategies = [
      {
        enabled = true
        symbol = "SHIBUSDT"
        type = "range"
        parameters = {
          lower-bound = "0.00002400"
          upper-bound = "0.00002550"
        }
      }
    ]
    ```
    Each strategy can be enabled/disabled and configured with specific parameters.

* **Data Handlers:** Data sources are configured in the `bot.data-handlers` section:
    ```hocon
    data-handlers = [
      {
        enabled = true
        type = "binance"
        parameters = {
          base-url = "wss://stream.binance.com:9443/ws"
        }
      }
    ]
    ```

* **Logging Level:** The verbosity of the logs can be controlled in `src/main/resources/logback.xml`. Change the root level from `INFO` to `DEBUG` to see more detailed output.
    ```xml
    <!-- in logback.xml -->
    <root level="INFO"> <!-- Change to DEBUG for more logs -->
        <appender-ref ref="STDOUT" />
    </root>
    ```

> ### ⚠️ Security Warning
> In future commits that require API keys, they must **never** be hardcoded. The architecture is designed to load them from a secure secrets manager like **HashiCorp Vault** at runtime.

## 🗺️ Project Roadmap

This project is a work in progress. Here are the planned future enhancements:

* [x] **Phase 1: Strategy & Execution** *(Partially Completed - July 25, 2025)*
    * [x] Implement the `StrategyEngine` to consume `PriceTick`s.
    * [x] Create a simple `RangeStrategy` class that can be configured via configuration file.
    * [ ] Implement the `ExecutionHandler` to place mock orders and manage their lifecycle.

* [ ] **Phase 2: Backtesting & Validation**
    * [ ] Integrate the **`roboquant`** library to create a robust backtesting suite.
    * [ ] Create a `HistoricalDataHandler` to feed the backtester from CSV files.
    * [ ] Conduct extensive paper trading on an exchange testnet (e.g., Binance Testnet).

* [ ] **Phase 3: Live Trading & Fortification**
    * [ ] Implement real, signed order execution with proper error handling and idempotency (`clientOid`).
    * [ ] Implement **One-Cancels-the-Other (OCO)** orders for simultaneous Stop-Loss and Take-Profit.
    * [ ] Add a **Fixed Percentage** position sizing model to manage risk.
    * [ ] Integrate **HashiCorp Vault** for secure API key management.

* [ ] **Phase 4: Deployment & Monitoring**
    * [ ] Dockerize the application for portable deployment.
    * [ ] Deploy to a cloud provider (e.g., Heroku or AWS).
    * [ ] Set up a monitoring pipeline with **Prometheus**, **Grafana**, and **Alertmanager**.

* [ ] **Phase 5: Advanced Strategies & Optimization**
    * [ ] Integrate the **`ta4j`** library to experiment with indicator-based strategies (e.g., EMA Crossover, RSI).
    * [ ] Investigate low-latency JVM tuning with **ZGC** or **Shenandoah** garbage collectors.

## 🤝 Contributing

This is an educational project, but contributions, suggestions, and feedback are always welcome! Feel free to open an issue or submit a pull request.

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

## 📜 License

Distributed under the MIT License. See `LICENSE.txt` for more information.
