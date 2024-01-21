package kipi.controllers

import kipi.dto.ElementCreatedResponse
import kipi.dto.TransactionDraft
import kipi.services.TransactionService

class TransactionCreateController(
    private val transactionService: TransactionService
) {
    fun handle(userId: Long, accountId: Long, transactionDraft: TransactionDraft) =
        ElementCreatedResponse(transactionService.createTransaction(userId, accountId, transactionDraft))
}