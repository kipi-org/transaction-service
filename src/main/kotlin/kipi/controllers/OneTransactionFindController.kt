package kipi.controllers

import kipi.dto.Transaction
import kipi.exceptions.TransactionNotExistException
import kipi.services.TransactionService

class OneTransactionFindController(
    private val transactionService: TransactionService
) {
    fun handle(userId: Long, transactionId: Long): Transaction {
        val transaction = transactionService.findTransaction(transactionId)
        if (transaction == null || transaction.category.userId != userId) throw TransactionNotExistException("transaction.not.exist")

        return transaction
    }
}