package kipi.controllers

import kipi.services.StatisticsService
import java.time.LocalDateTime

class CategoriesStatisticsController(
    private val statisticsService: StatisticsService
) {
    fun handle(accountIds: List<Long>, from: LocalDateTime?, to: LocalDateTime?) =
        statisticsService.getCategoriesStatistics(accountIds, from, to)
}