package com.example

import com.example.config.Config
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext

class DatabaseProvider :
    DatabaseProviderContract,
    KoinComponent {
    private val config by inject<Config>()
    private val dispatcher: CoroutineContext = Dispatchers.IO

    override fun init() {
        Database.connect(hikari(config))
    }

    private fun hikari(mainConfig: Config): HikariDataSource {
        val hikariConfig =
            HikariConfig().apply {
                jdbcUrl = mainConfig.postgresUrl
                driverClassName = "org.postgresql.Driver"
                username = mainConfig.postgresUser
                password = mainConfig.postgresPassword
                maximumPoolSize = 10
                isAutoCommit = false
            }
        return HikariDataSource(hikariConfig)
    }

    override suspend fun <T> dbQuery(block: () -> T): T =
        withContext(dispatcher) {
            transaction { block() }
        }
}

interface DatabaseProviderContract {
    fun init()

    suspend fun <T> dbQuery(block: () -> T): T
}
