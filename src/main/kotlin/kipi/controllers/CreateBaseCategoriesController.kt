package kipi.controllers

import kipi.services.CategoryService

class CreateBaseCategoriesController(
    private val categoriesService: CategoryService
) {
    fun handle(userId: Long) = categoriesService.createBaseCategories(userId)
}