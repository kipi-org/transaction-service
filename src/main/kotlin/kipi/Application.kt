package kipi

import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpStatusCode.Companion.Forbidden
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kipi.db.DataSourceConfigurator
import kipi.db.DbMigration
import kipi.dto.ErrorResponse
import kipi.exceptions.*
import kipi.mappers.JsonMapper.mapper
import org.jetbrains.exposed.sql.Database

fun main() {
    embeddedServer(Netty, port = 7001, host = "0.0.0.0", module = Application::init)
        .start(wait = true)
}

fun Application.init() {
    install(ContentNegotiation) {
        register(Json, JacksonConverter(mapper))
    }

    install(StatusPages) {
        exception<CategoryException> { call, cause ->
            call.respond(Forbidden, ErrorResponse(cause.message))
        }

        exception<LimitCreateException> { call, cause ->
            call.respond(Forbidden, ErrorResponse(cause.message))
        }

        exception<InvalidTinkoffDataException> { call, cause ->
            call.respond(Forbidden, ErrorResponse(cause.message))
        }

        exception<TransactionNotExistException> { call, cause ->
            call.respond(NotFound, ErrorResponse(cause.message))
        }
    }

    val deps = Dependencies()

    initMigration(deps.config)
    routes(deps)
}

private fun initMigration(config: Config) {
    DataSourceConfigurator(config).also {
        val dataSource = it.createDataSource()
        Database.connect(dataSource)
        DbMigration(dataSource).migrate()
    }
}