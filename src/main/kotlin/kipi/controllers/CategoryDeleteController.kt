package kipi.controllers

import kipi.services.CategoryService

class CategoryDeleteController(
    private val categoryService: CategoryService
) {
    fun handle(userId: Long, categoryId: Long) =
        categoryService.deleteCategory(userId, categoryId)
}