package kipi

import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kipi.dto.*
import java.time.LocalDateTime

fun Application.routes(deps: Dependencies) = with(deps) {
    routing {
        get("/health") {
            call.respond(OK)
        }

        route("/customer/{userId}") {
            post("/category") {
                call.respond(OK, categoryCreateController.handle(call.userId, call.receive()))
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
                post {
                    call.respond(OK, limitCreateController.handle(call.userId, call.receive(), call.accountsIds))
                }

                route("/{limitId}") {
                    delete {
                        limitDeleteController.handle(call.userId, call.limitId)

                        call.respond(OK)
                    }

                    put {
                        updateLimitController.handle(call.userId, call.limitId, call.receive())

                        call.respond(OK)
                    }
                }
            }

            post("/goal") {
                call.respond(OK, goalCreateController.handle(call.userId, call.receive(), call.accountsIds))
            }

            delete("/goal/{goalId}") {
                goalDeleteController.handle(call.userId, call.goalId)

                call.respond(OK)
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

            get("/goals") {
                call.respond(OK, goalsFindController.handle(call.userId))
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

                put {
                    transactionUpdateController.handle(call.userId, call.transactionId, call.receive())

                    call.respond(OK)
                }
            }

            route("/account/{accountId}") {
                post("/transaction") {
                    call.respond(OK, transactionCreateController.handle(call.userId, call.accountId, call.receive()))
                }
                post("/transactions/foreign") {
                    call.respond(OK, foreignTransactionCreateController.handle(call.accountId, call.receive()))
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

private val ApplicationCall.goalId: Long
    get() = this.parameters.getOrFail("goalId").toLong()

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