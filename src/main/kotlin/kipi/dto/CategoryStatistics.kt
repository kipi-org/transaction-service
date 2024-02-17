package kipi.dto

import java.math.BigDecimal


data class CategoryStatistics(
    val category: Category,
    val amount: BigDecimal,
    val type: OperationType
)
