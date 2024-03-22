package kipi.repositories

import kipi.dao.Categories
import kipi.dao.TransactionTypes
import kipi.dao.Transactions
import kipi.dao.Transactions.accountId
import kipi.dto.*
import kipi.dto.Transaction
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
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
            it[date] = transactionDraft.date ?: now()
            it[description] = transactionDraft.description
            it[foreignId] = transactionDraft.foreignId
        }[Transactions.id]
    }

    fun createManyTransactions(accountId: Long, transactionDrafts: List<TransactionDraft>) = transaction {
        Transactions.batchInsert(transactionDrafts) { transactionDraft ->
            this[Transactions.accountId] = accountId
            this[Transactions.txTypeId] =
                TransactionTypes.select { TransactionTypes.name eq transactionDraft.type.toString() }
                    .map { row -> row[TransactionTypes.id] }.first()
            this[Transactions.categoryId] = transactionDraft.categoryId
            this[Transactions.amount] = transactionDraft.amount
            this[Transactions.date] = transactionDraft.date ?: now()
            this[Transactions.description] = transactionDraft.description
            this[Transactions.foreignId] = transactionDraft.foreignId
        }
    }

    fun updateTransaction(transactionId: Long, updates: TransactionUpdates) = transaction {
        Transactions.update({ Transactions.id eq transactionId }) {
            updates.categoryId?.let { id -> it[categoryId] = id }
            updates.description?.let { desc -> it[description] = desc }
        }
    }

    fun findTransactions(
        accountIds: List<Long>,
        from: LocalDateTime? = null,
        to: LocalDateTime? = null,
        page: Int? = null,
        pageSize: Int? = null,
        categoryId: Long? = null
        ): List<Transaction> = transaction {
        (Transactions innerJoin TransactionTypes innerJoin Categories).select {
            val isInList = accountId inList accountIds
            val isFrom = if (from != null) (Transactions.date greaterEq from) else Op.TRUE
            val isTo = if (to != null) (Transactions.date lessEq to) else Op.TRUE
            val categoryIdCheck = if (categoryId != null) (Transactions.categoryId eq categoryId) else Op.TRUE

            isInList and isFrom and isTo and categoryIdCheck
        }.orderBy(Transactions.date to SortOrder.DESC)
            .apply { if (page != null && pageSize != null) this.limit(pageSize, page.toLong() * pageSize.toLong()) }
            .map { mapToTransaction(it) }
    }

    fun findTransaction(id: Long): Transaction? = transaction {
        (Transactions innerJoin TransactionTypes innerJoin Categories).select {
            Transactions.id eq id
        }.map { mapToTransaction(it) }.firstOrNull()
    }

    fun deleteTransaction(id: Long) = transaction {
        Transactions.deleteWhere { Transactions.id eq id }
    }

    fun deleteTransactionsWithAccountsIds(accountsIds: List<Long>) = transaction {
        Transactions.deleteWhere { accountId inList accountsIds }
    }

    private fun mapToTransaction(resultRow: ResultRow): Transaction =
        Transaction(
            id = resultRow[Transactions.id],
            accountId = resultRow[accountId],
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
            description = resultRow[Transactions.description],
            foreignId = resultRow[Transactions.foreignId]
        )
}