package kipi.controllers

import kipi.dto.ElementCreatedResponse
import kipi.dto.LimitDraft
import kipi.services.LimitService

class LimitCreateController(
    private val limitService: LimitService
) {
    fun handle(userId: Long, limitDraft: LimitDraft, accountsIds: List<Long>?) =
        ElementCreatedResponse(limitService.createLimit(userId, limitDraft, accountsIds))
}