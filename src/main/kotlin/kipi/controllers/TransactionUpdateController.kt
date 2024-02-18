package kipi.controllers

import kipi.dto.TransactionUpdates
import kipi.exceptions.CategoryException
import kipi.exceptions.TransactionNotExistException
import kipi.services.CategoryService
import kipi.services.TransactionService

class TransactionUpdateController(
    private val transactionService: TransactionService,
    private val categoryService: CategoryService
) {
    fun handle(userId: Long, transactionId: Long, transactionUpdates: TransactionUpdates) {
        val transaction = transactionService.findTransaction(transactionId)
        if (transaction == null || transaction.category.userId != userId) throw TransactionNotExistException("transaction.not.exist")
        transactionUpdates.categoryId?.let {
            categoryService.findCategory(userId, it) ?: throw CategoryException("category.not.exist")
        }

        transactionService.updateTransaction(transactionId, transactionUpdates)
    }
}