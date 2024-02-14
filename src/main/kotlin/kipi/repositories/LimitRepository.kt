package kipi.repositories

import kipi.dao.Categories
import kipi.dao.Limits
import kipi.dao.Limits.amount
import kipi.dao.Limits.categoryId
import kipi.dao.Limits.currentAmount
import kipi.dao.Limits.id
import kipi.dto.Category
import kipi.dto.Limit
import kipi.dto.LimitDraft
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class LimitRepository {

    fun createLimit(limitDraft: LimitDraft) = transaction {
        Limits.insert {
            it[amount] = limitDraft.amount
            it[currentAmount] = limitDraft.currentAmount
            it[categoryId] = limitDraft.categoryId
        }[Limits.id]
    }

    fun findLimits(categoriesIds: List<Long>) = transaction {
        (Limits innerJoin Categories).select {
            categoryId inList categoriesIds
        }.map { mapToLimit(it) }
    }

    fun deleteLimit(limitId: Long) = transaction {
        Limits.deleteWhere {
            id eq limitId
        }
    }

    private fun mapToLimit(resultRow: ResultRow) = Limit(
        id = resultRow[id],
        amount = resultRow[amount],
        currentAmount = resultRow[currentAmount],
        category = Category(
            id = resultRow[Categories.id],
            userId = resultRow[Categories.userId],
            name = resultRow[Categories.name],
            iconUrl = resultRow[Categories.iconUrl],
            colorCode = resultRow[Categories.colorCode]
        )
    )
}