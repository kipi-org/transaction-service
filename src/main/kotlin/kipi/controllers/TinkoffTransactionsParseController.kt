package kipi.controllers

import kipi.dto.tinkoff.TinkoffXmlRequest
import kipi.services.TinkoffTransactionsParseService
import kipi.services.TransactionService

class TinkoffTransactionsParseController(
    private val tinkoffTransactionsParseService: TinkoffTransactionsParseService,
    private val transactionService: TransactionService
) {
    fun handle(userId: Long, tinkoffXmlRequest: TinkoffXmlRequest) {
        val transactionDrafts = tinkoffTransactionsParseService.parseTransactions(userId, tinkoffXmlRequest)

        transactionService.createManyTransactionsWithForeignId(tinkoffXmlRequest.accountId, transactionDrafts)
    }
}