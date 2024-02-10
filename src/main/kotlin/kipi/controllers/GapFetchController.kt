package kipi.controllers

import kipi.dto.GapType
import kipi.services.TransactionService

class GapFetchController(
    private val transactionService: TransactionService
) {
    fun handle(
        accountIds: List<Long>,
        gapType: GapType,
        page: Int = 0,
        pageSize: Int = 15,
    ) = transactionService.getTransactionsGaps(accountIds, gapType, page, pageSize)
}