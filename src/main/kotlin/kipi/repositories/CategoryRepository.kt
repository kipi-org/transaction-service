package kipi.repositories

import kipi.dao.Categories
import kipi.dao.Categories.colorCode
import kipi.dao.Categories.iconUrl
import kipi.dao.Categories.id
import kipi.dao.Categories.name
import kipi.dao.Categories.userId
import kipi.dto.Category
import kipi.dto.CategoryDraft
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class CategoryRepository {

    fun createCategory(userId: Long, categoryDraft: CategoryDraft) = transaction {
        Categories.insert {
            it[Categories.userId] = userId
            it[name] = categoryDraft.name
            it[iconUrl] = categoryDraft.iconUrl
            it[colorCode] = categoryDraft.colorCode
        }[Categories.id]
    }

    fun findUserCategories(userId: Long) = transaction {
        Categories.select {
            Categories.userId eq userId
        }.map { mapToCategory(it) }
    }

    fun deleteCategory(userId: Long, categoryId: Long) = transaction {
        Categories.deleteWhere {
            (id eq categoryId) and (Categories.userId eq userId)
        }
    }

    private fun mapToCategory(resultRow: ResultRow) = Category(
        id = resultRow[id],
        userId = resultRow[userId],
        name = resultRow[name],
        iconUrl = resultRow[iconUrl],
        colorCode = resultRow[colorCode]
    )
}