package kipi.controllers

import kipi.dto.TransactionDraft
import kipi.services.TransactionService

class ForeignTransactionCreateController(
    private val transactionService: TransactionService
) {
    fun handle(accountId: Long, transactionDrafts: List<TransactionDraft>) {
        transactionService.createManyTransactionsWithForeignId(accountId, transactionDrafts)
    }
}