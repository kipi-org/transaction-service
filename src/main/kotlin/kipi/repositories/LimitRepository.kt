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
import kipi.dto.LimitUpdates
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
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

    fun updateLimits(limitId: Long, limitUpdates: LimitUpdates) = transaction {
        Limits.update ({ Limits.id eq limitId }){
            it[amount] = limitUpdates.amount
        }
    }

    fun deleteLimit(limitId: Long) = transaction {
        Limits.deleteWhere {
            id eq limitId
        }
    }

    fun deleteLimits(limitsIds: List<Long>) = transaction {
        Limits.deleteWhere {
            id inList limitsIds
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