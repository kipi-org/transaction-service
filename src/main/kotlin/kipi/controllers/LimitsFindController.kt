package kipi.controllers

import kipi.services.LimitService

class LimitsFindController(
    private val limitService: LimitService
) {
    fun handle(userId: Long) = limitService.findLimits(userId)
}