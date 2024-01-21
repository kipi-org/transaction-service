package kipi.controllers

import kipi.services.GoalService

class GoalsFindController(
    private val goalService: GoalService
) {
    fun handle(userId: Long) = goalService.findGoals(userId)
}