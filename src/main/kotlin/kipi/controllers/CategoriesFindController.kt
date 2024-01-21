package kipi.controllers

import kipi.services.CategoryService

class CategoriesFindController(
    private val categoryService: CategoryService
) {
    fun handle(userId: Long) = categoryService.findCategories(userId)
}