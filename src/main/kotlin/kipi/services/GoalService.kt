package kipi.services

import kipi.dto.Goal
import kipi.dto.GoalDraft
import kipi.exceptions.CategoryException
import kipi.exceptions.GoalCreateException
import kipi.repositories.GoalRepository
import kipi.utils.TransactionUtils.filterByCategoryIdAndLastMonth
import java.math.BigDecimal.ZERO

class GoalService(
    private val goalRepository: GoalRepository,
    private val categoryService: CategoryService,
    private val transactionService: TransactionService
) {
    fun createGoal(userId: Long, goalDraft: GoalDraft, accountIds: List<Long>?): Long {
        val existCategories = categoryService.findCategories(userId)
        val goals = goalRepository.findGoals(existCategories.map { it.id })

        if (existCategories.none { it.id == goalDraft.categoryId }) throw CategoryException("category.not.exist")
        if (goals.any { it.category.id == goalDraft.categoryId }) throw GoalCreateException("goal.with.category.exist")

        val draft = if (goalDraft.currentAmount == ZERO) goalDraft.copy(
            currentAmount = transactionService.getTransactions(accountIds!!)
                .filterByCategoryIdAndLastMonth(goalDraft.categoryId)
        )
        else goalDraft
        return goalRepository.createGoal(draft)
    }

    fun findGoals(userId: Long): List<Goal> {
        val existCategories = categoryService.findCategories(userId)

        return goalRepository.findGoals(existCategories.map { it.id })
    }

    fun deleteGoal(userId: Long, goalId: Long) {
        val categories = categoryService.findCategories(userId)
        val limits = goalRepository.findGoals(categories.map { it.id })
        if (limits.none { it.id == goalId }) return

        goalRepository.deleteGoal(goalId)
    }
}