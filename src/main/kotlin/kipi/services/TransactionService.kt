package kipi.services

import kipi.dto.TransactionDraft
import kipi.exceptions.CategoryException
import kipi.repositories.TransactionRepository

class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val categoryService: CategoryService
) {
    fun createTransaction(userId: Long, accountId: Long, transactionDraft: TransactionDraft): Long {
        val categories = categoryService.findCategories(userId)
        if (categories.none { it.id == transactionDraft.categoryId }) throw CategoryException("This category not exist")

        return transactionRepository.createTransaction(accountId, transactionDraft)
    }

    fun getTransactions(accountIds: List<Long>) = transactionRepository.findTransactions(accountIds)

    fun deleteTransaction(id: Long) = transactionRepository.deleteTransaction(id)
}