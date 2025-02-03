package com.example.config

import io.github.cdimascio.dotenv.Dotenv

class Config {
    private val dotenv: Dotenv =
        Dotenv
            .configure()
            .ignoreIfMissing()
            .load()

    val postgresUrl: String by lazy { dotenv["POSTGRES_URL"] ?: throw IllegalArgumentException("POSTGRES_URL is not defined in .env") }
    val postgresUser: String by lazy { dotenv["POSTGRES_USER"] ?: throw IllegalArgumentException("POSTGRES_USER is not defined in .env") }
    val postgresPassword: String by lazy {
        dotenv["POSTGRES_PASSWORD"]
            ?: throw IllegalArgumentException("POSTGRES_PASSWORD is not defined in .env")
    }
}
