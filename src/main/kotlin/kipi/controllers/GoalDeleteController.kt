package kipi.controllers

import kipi.services.GoalService

class GoalDeleteController(
    private val goalService: GoalService
) {
    fun handle(userId: Long, goalId: Long) = goalService.deleteGoal(userId, goalId)
}