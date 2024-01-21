package kipi.repositories

import kipi.dao.Categories
import kipi.dao.TransactionTypes
import kipi.dao.Transactions
import kipi.dto.Category
import kipi.dto.Transaction
import kipi.dto.TransactionDraft
import kipi.dto.TransactionType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime.now

class TransactionRepository {

    fun createTransaction(accountId: Long, transactionDraft: TransactionDraft) = transaction {
        Transactions.insert {
            it[Transactions.accountId] = accountId
            it[txTypeId] =
                TransactionTypes.select { TransactionTypes.name eq transactionDraft.type.toString() }
                    .map { row -> row[TransactionTypes.id] }.first()
            it[categoryId] = transactionDraft.categoryId
            it[amount] = transactionDraft.amount
            it[date] = now()
            it[description] = transactionDraft.description
        }[Transactions.id]
    }

    fun findTransactions(accountIds: List<Long>): List<Transaction> = transaction {
        (Transactions innerJoin TransactionTypes innerJoin Categories).select {
            Transactions.accountId inList accountIds
        }.map { mapToTransaction(it) }
    }

    fun deleteTransaction(id: Long) = transaction {
        Transactions.deleteWhere { Transactions.id eq id }
    }

    private fun mapToTransaction(resultRow: ResultRow): Transaction =
        Transaction(
            id = resultRow[Transactions.id],
            accountId = resultRow[Transactions.accountId],
            type = TransactionType.valueOf(resultRow[TransactionTypes.name]),
            amount = resultRow[Transactions.amount],
            date = resultRow[Transactions.date],
            category = Category(
                id = resultRow[Categories.id],
                userId = resultRow[Categories.userId],
                name = resultRow[Categories.name],
                iconUrl = resultRow[Categories.iconUrl],
                colorCode = resultRow[Categories.colorCode]
            ),
            description = resultRow[Transactions.description]
        )
}