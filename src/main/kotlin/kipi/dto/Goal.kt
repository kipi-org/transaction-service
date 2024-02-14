package kipi.dto

import java.io.Serializable
import java.math.BigDecimal

data class Goal(
    val id: Long,
    val amount: BigDecimal,
    val currentAmount: BigDecimal,
    val category: Category
) : Serializable