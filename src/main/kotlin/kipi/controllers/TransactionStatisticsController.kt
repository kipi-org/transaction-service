package kipi.controllers

import kipi.services.TransactionService

class TransactionStatisticsController(
    private val transactionService: TransactionService
) {
    fun handle(accountIds: List<Long>) = transactionService.getTransactionsStatistics(accountIds)
}