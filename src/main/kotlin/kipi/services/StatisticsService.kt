package kipi.services

import kipi.dto.CategoryStatistics
import kipi.dto.OperationType.INCOME
import kipi.dto.OperationType.OUTCOME
import java.math.BigDecimal.ZERO
import java.time.LocalDateTime

class StatisticsService(
    private val transactionService: TransactionService
) {
    fun getCategoriesStatistics(
        accountsIds: List<Long>,
        from: LocalDateTime? = null,
        to: LocalDateTime? = null
    ): List<CategoryStatistics> {
        val transactions = transactionService.getTransactions(accountsIds, from, to)
        val categoryIdToTransactions = transactions.groupBy { it.category.id }

        return categoryIdToTransactions.values.mapNotNull {
            val sum = it.sumOf { tx -> tx.amount }

            if (sum == ZERO) {
                null
            } else CategoryStatistics(
                category = it.first().category,
                amount = sum,
                type = if (sum > ZERO) INCOME else OUTCOME
            )
        }
    }

}