package kipi

import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kipi.dto.CategoryDraft
import kipi.dto.GoalDraft
import kipi.dto.LimitDraft
import kipi.dto.TransactionDraft

fun Application.routes(deps: Dependencies) = with(deps) {
    routing {
        get("/health"){
            call.respond(OK)
        }

        route("/customer/{userId}") {
            post<CategoryDraft>("/category") {
                call.respond(OK, categoryCreateController.handle(call.userId, it))
            }

            delete("/category/{categoryId}") {
                categoryDeleteController.handle(call.userId, call.categoryId)

                call.respond(OK)
            }

            post<LimitDraft>("/limit") {
                call.respond(OK, limitCreateController.handle(call.userId, it, call.accountsIds))
            }

            delete("/limit/{limitId}") {
                limitDeleteController.handle(call.userId, call.limitId)

                call.respond(OK)
            }

            post<GoalDraft>("/goal") {
                call.respond(OK, goalCreateController.handle(call.userId, it, call.accountsIds))
            }

            delete("/goal/{goalId}") {
                goalDeleteController.handle(call.userId, call.goalId)

                call.respond(OK)
            }

            get("/categories") {
                call.respond(OK, categoriesFindController.handle(call.userId))
            }

            get("/limits") {
                call.respond(OK, limitsFindController.handle(call.userId))
            }

            get("/goals") {
                call.respond(OK, goalsFindController.handle(call.userId))
            }

            get("/transactions") {
                call.respond(OK, transactionFetchController.handle(call.accountsIds))
            }

            delete("/transaction/{transactionId}") {
                transactionDeleteController.handle(call.userId, call.transactionId)

                call.respond(OK)
            }

            route("/account/{accountId}") {
                post<TransactionDraft>("/transaction") {
                    call.respond(OK, transactionCreateController.handle(call.userId, call.accountId, it))
                }
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

private val ApplicationCall.categoryId: Long
    get() = this.parameters.getOrFail("categoryId").toLong()

private val ApplicationCall.limitId: Long
    get() = this.parameters.getOrFail("limitId").toLong()

private val ApplicationCall.goalId: Long
    get() = this.parameters.getOrFail("goalId").toLong()

private val ApplicationCall.transactionId: Long
    get() = this.parameters.getOrFail("transactionId").toLong()
