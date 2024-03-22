package kipi.services

import kipi.dto.Limit
import kipi.dto.LimitDraft
import kipi.exceptions.CategoryException
import kipi.exceptions.LimitCreateException
import kipi.repositories.LimitRepository
import kipi.utils.TransactionUtils.filterByCategoryIdAndLastMonth
import java.math.BigDecimal.ZERO

class LimitService(
    private val limitRepository: LimitRepository,
    private val categoryService: CategoryService,
    private val transactionService: TransactionService
) {
    fun createLimit(userId: Long, limitDraft: LimitDraft, accountIds: List<Long>? = null): Long {
        val existCategories = categoryService.findCategories(userId)
        val limits = limitRepository.findLimits(existCategories.map { it.id })

        if (existCategories.none { it.id == limitDraft.categoryId }) throw CategoryException("category.not.exist")
        if (limits.any { it.category.id == limitDraft.categoryId }) throw LimitCreateException("limit.with.category.exist")

        val draft = if (limitDraft.currentAmount == ZERO) limitDraft.copy(
            currentAmount = transactionService.getTransactions(accountIds!!)
                .filterByCategoryIdAndLastMonth(limitDraft.categoryId)
        )
        else limitDraft
        return limitRepository.createLimit(draft)
    }

    fun findLimits(userId: Long): List<Limit> {
        val existCategories = categoryService.findCategories(userId)

        return limitRepository.findLimits(existCategories.map { it.id })
    }

    fun deleteLimit(userId: Long, limitId: Long) {
        val categories = categoryService.findCategories(userId)
        val limits = limitRepository.findLimits(categories.map { it.id })
        if (limits.none { it.id == limitId }) return

        limitRepository.deleteLimit(limitId)
    }

    fun deleteAllLimits(userId: Long) {
        val categories = categoryService.findCategories(userId)
        val limitsIds = limitRepository.findLimits(categories.map { it.id }).map { it.id }
        if (limitsIds.isEmpty()) return

        limitRepository.deleteLimits(limitsIds)
    }
}