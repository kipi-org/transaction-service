package kipi.controllers

import kipi.dto.ElementCreatedResponse
import kipi.dto.GoalDraft
import kipi.services.GoalService

class GoalCreateController(
    private val goalService: GoalService
) {
    fun handle(userId: Long, goalDraft: GoalDraft, accountsIds: List<Long>?) =
        ElementCreatedResponse(goalService.createGoal(userId, goalDraft, accountsIds))
}