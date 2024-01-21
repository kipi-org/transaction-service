package kipi.controllers

import kipi.dto.CategoryDraft
import kipi.dto.ElementCreatedResponse
import kipi.services.CategoryService

class CategoryCreateController(
    private val categoryService: CategoryService
) {
    fun handle(userId: Long, categoryDraft: CategoryDraft) =
        ElementCreatedResponse(categoryService.createCategory(userId, categoryDraft))
}