package kipi.utils

import kipi.dto.Transaction
import java.time.LocalDate.now

object TransactionUtils {
    fun List<Transaction>.filterByCategoryIdAndLastMonth(categoryId: Long) =
        filter { it.category.id == categoryId }
            .filter { it.date >= now().withDayOfMonth(1).atStartOfDay() }
            .sumOf { it.amount }
}