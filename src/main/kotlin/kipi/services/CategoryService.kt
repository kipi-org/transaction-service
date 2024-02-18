package kipi.services

import kipi.Config
import kipi.dto.Category
import kipi.dto.CategoryDraft
import kipi.exceptions.CategoryException
import kipi.repositories.CategoryRepository

class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val config: Config
) {
    fun createCategory(userId: Long, categoryDraft: CategoryDraft): Long {
        val existCategories = categoryRepository.findUserCategories(userId)

        if (existCategories.any { it.name == categoryDraft.name }) throw CategoryException("category.name.exist")

        return categoryRepository.createCategory(userId, categoryDraft)
    }

    fun findCategories(userId: Long): List<Category> = categoryRepository.findUserCategories(userId)

    fun deleteCategory(userId: Long, categoryId: Long) = categoryRepository.deleteCategory(userId, categoryId)

    fun createBaseCategories(userId: Long) {
        val defaultCategories = config.defaultCategories

        defaultCategories.categories.map {
            CategoryDraft(
                name = it.name,
                iconUrl = it.iconUrl,
                colorCode = defaultCategories.colors.random()
            )
        }.forEach {
            createCategory(userId, it)
        }
    }
}