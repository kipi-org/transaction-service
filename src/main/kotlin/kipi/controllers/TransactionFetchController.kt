package kipi.controllers

import kipi.services.TransactionService
import java.time.LocalDateTime

class TransactionFetchController(
    private val transactionService: TransactionService
) {
    fun handle(accountIds: List<Long>, categoryId: Long?, from: LocalDateTime?, to: LocalDateTime?, page: Int, pageSize: Int) =
        transactionService.getTransactions(accountIds, from, to, page, pageSize, categoryId)
}