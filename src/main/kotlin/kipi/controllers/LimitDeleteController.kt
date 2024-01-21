package kipi.controllers

import kipi.services.LimitService

class LimitDeleteController(
    private val limitService: LimitService
) {
    fun handle(userId: Long, limitId: Long) = limitService.deleteLimit(userId, limitId)
}