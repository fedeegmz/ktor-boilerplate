package com.example

import com.example.config.Config
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain
        .main(args)
}

fun Application.module() {
    val config = Config()
    val database = DatabaseProvider()

    install(Koin) {
        modules(
            module {
                single { config }
                single<DatabaseProviderContract> { database }
            },
        )
    }
    install(ContentNegotiation) {
        json()
    }

    database.init()
    configureRouting()
}
