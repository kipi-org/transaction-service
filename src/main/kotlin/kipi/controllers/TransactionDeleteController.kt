package kipi.controllers

import kipi.services.TransactionService

class TransactionDeleteController(
    private val transactionService: TransactionService
) {
    fun handle(userId: Long, transactionId: Long) = transactionService.deleteTransaction(transactionId)
}