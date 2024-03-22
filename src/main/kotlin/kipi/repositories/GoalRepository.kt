package kipi.repositories

import kipi.dao.Categories
import kipi.dao.Goals
import kipi.dao.Goals.amount
import kipi.dao.Goals.categoryId
import kipi.dao.Goals.currentAmount
import kipi.dao.Goals.id
import kipi.dto.Category
import kipi.dto.Goal
import kipi.dto.GoalDraft
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class GoalRepository {
    fun createGoal(goalDraft: GoalDraft) = transaction {
        Goals.insert {
            it[amount] = goalDraft.amount
            it[currentAmount] = goalDraft.currentAmount
            it[categoryId] = goalDraft.categoryId
        }[Goals.id]
    }

    fun findGoals(categoriesIds: List<Long>) = transaction {
        (Goals innerJoin Categories).select {
            categoryId inList categoriesIds
        }.map { mapToGoal(it) }
    }

    fun deleteGoal(goalId: Long) = transaction {
        Goals.deleteWhere {
            id eq goalId
        }
    }

    fun deleteGoals(goalsIds: List<Long>) = transaction {
        Goals.deleteWhere {
            id inList goalsIds
        }
    }

    private fun mapToGoal(resultRow: ResultRow) = Goal(
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