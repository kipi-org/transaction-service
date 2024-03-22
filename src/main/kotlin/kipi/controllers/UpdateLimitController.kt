package kipi.controllers

import kipi.dto.LimitUpdates
import kipi.services.LimitService

class UpdateLimitController(
    private val limitService: LimitService
) {
    fun handle(userId: Long, limitId: Long, limitUpdates: LimitUpdates) =
        limitService.updateLimit(userId, limitId, limitUpdates)
}