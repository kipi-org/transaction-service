package kipi

import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kipi.dto.*
import kipi.dto.tinkoff.TinkoffXmlRequest
import java.time.LocalDateTime

fun Application.routes(deps: Dependencies) = with(deps) {
    routing {
        get("/health") {
            call.respond(OK)
        }

        route("/customer/{userId}") {
            post<CategoryDraft>("/category") {
                call.respond(OK, categoryCreateController.handle(call.userId, it))
            }

            delete {
                deleteUserInfoController.handle(call.userId, call.accountsIds)

                call.respond(OK)
            }

            delete("/category/{categoryId}") {
                categoryDeleteController.handle(call.userId, call.categoryId!!)

                call.respond(OK)
            }

            route("/limit") {
                post<LimitDraft> {
                    call.respond(OK, limitCreateController.handle(call.userId, it, call.accountsIds))
                }

                route("/{limitId}") {
                    delete {
                        limitDeleteController.handle(call.userId, call.limitId)

                        call.respond(OK)
                    }

                    put<LimitUpdates> {
                        updateLimitController.handle(call.userId, call.limitId, it)

                        call.respond(OK)
                    }
                }
            }

            route("/categories") {
                get {
                    call.respond(OK, categoriesFindController.handle(call.userId))
                }

                post("/base") {
                    createBaseCategoriesController.handle(call.userId)
                    call.respond(OK)
                }
            }

            get("/limits") {
                call.respond(OK, limitsFindController.handle(call.userId))
            }

            route("/transactions") {
                get {
                    call.respond(
                        OK,
                        transactionFetchController.handle(
                            call.accountsIds,
                            call.categoryId,
                            call.from,
                            call.to,
                            call.page,
                            call.pageSize
                        )
                    )
                }

                post<TinkoffXmlRequest>("/tinkoff") {
                    tinkoffTransactionsParseController.handle(call.userId, it)
                    call.respond(OK)
                }

                get("/gaps/{gapType}") {
                    call.respond(
                        OK,
                        gapFetchController.handle(call.accountsIds, call.gapType, call.page, call.pageSize, call.categoryId)
                    )
                }
            }

            route("/transaction/{transactionId}") {
                delete {
                    transactionDeleteController.handle(call.userId, call.transactionId)

                    call.respond(OK)
                }

                get {
                    call.respond(
                        OK,
                        oneTransactionFindController.handle(call.userId, call.transactionId)
                    )
                }

                put<TransactionUpdates> {
                    transactionUpdateController.handle(call.userId, call.transactionId, it)

                    call.respond(OK)
                }
            }

            route("/account/{accountId}") {
                post<TransactionDraft>("/transaction") {
                    call.respond(OK, transactionCreateController.handle(call.userId, call.accountId, it))
                }
            }

            get("/categories/statistics") {
                call.respond(OK, categoriesStatisticsController.handle(call.accountsIds, call.from, call.to))
            }
        }
    }
}

private val ApplicationCall.accountsIds: List<Long>
    get() = this.parameters["accountsIds"]?.split(",")?.map { it.toLong() } ?: emptyList()

private val ApplicationCall.userId: Long
    get() = this.parameters.getOrFail("userId").toLong()

private val ApplicationCall.accountId: Long
    get() = this.parameters.getOrFail("accountId").toLong()

private val ApplicationCall.categoryId: Long?
    get() = this.parameters["categoryId"]?.toLong()

private val ApplicationCall.limitId: Long
    get() = this.parameters.getOrFail("limitId").toLong()

private val ApplicationCall.transactionId: Long
    get() = this.parameters.getOrFail("transactionId").toLong()

private val ApplicationCall.from: LocalDateTime?
    get() = this.parameters["from"]?.let { LocalDateTime.parse(it) }

private val ApplicationCall.to: LocalDateTime?
    get() = this.parameters["to"]?.let { LocalDateTime.parse(it) }

private val ApplicationCall.gapType: GapType
    get() = this.parameters.getOrFail("gapType").let { GapType.valueOf(it) }

private val ApplicationCall.page: Int
    get() = this.parameters["page"]?.toInt() ?: 0

private val ApplicationCall.pageSize: Int
    get() = this.parameters["pageSize"]?.toInt() ?: 15