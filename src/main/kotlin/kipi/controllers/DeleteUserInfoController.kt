package kipi.controllers

import kipi.services.CategoryService
import kipi.services.LimitService
import kipi.services.TransactionService

class DeleteUserInfoController(
    private val transactionService: TransactionService,
    private val limitService: LimitService,
    private val categoryService: CategoryService
) {
    fun handle(userId: Long, accountsIds: List<Long>) {
        transactionService.deleteTransactions(accountsIds)
        limitService.deleteAllLimits(userId)
        categoryService.deleteAllCategories(userId)
    }
}