package kipi.controllers

import kipi.services.TransactionService

class TransactionFetchController(
    private val transactionService: TransactionService
) {
    fun handle(accountIds: List<Long>) = transactionService.getTransactions(accountIds)
}