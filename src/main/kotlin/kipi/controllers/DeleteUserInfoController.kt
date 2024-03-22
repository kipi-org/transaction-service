package kipi.controllers

import kipi.services.CategoryService
import kipi.services.GoalService
import kipi.services.LimitService
import kipi.services.TransactionService

class DeleteUserInfoController(
    private val transactionService: TransactionService,
    private val goalService: GoalService,
    private val limitService: LimitService,
    private val categoryService: CategoryService
) {
    fun handle(userId: Long, accountsIds: List<Long>) {
        transactionService.deleteTransactions(accountsIds)
        goalService.deleteAllGoals(userId)
        limitService.deleteAllLimits(userId)
        categoryService.deleteAllCategories(userId)
    }
}