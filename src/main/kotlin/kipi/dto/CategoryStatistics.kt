package kipi.dto

import java.io.Serializable
import java.math.BigDecimal


data class CategoryStatistics(
    val category: Category,
    val amount: BigDecimal,
    val type: OperationType
) : Serializable
