package kipi.repositories

import kipi.dao.Goals
import kipi.dao.Goals.amount
import kipi.dao.Goals.categoryId
import kipi.dao.Goals.currentAmount
import kipi.dao.Goals.id
import kipi.dto.Goal
import kipi.dto.GoalDraft
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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
        Goals.select {
            categoryId inList categoriesIds
        }.map { mapToGoal(it) }
    }

    fun deleteGoal(goalId: Long) = transaction {
        Goals.deleteWhere {
            id eq goalId
        }
    }

    private fun mapToGoal(resultRow: ResultRow) = Goal(
        id = resultRow[id],
        amount = resultRow[amount],
        currentAmount = resultRow[currentAmount],
        categoryId = resultRow[categoryId]
    )
}