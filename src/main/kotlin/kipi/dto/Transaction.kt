package kipi.dto

import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(
    val id: Long,
    val accountId: Long,
    val type: TransactionType,
    val amount: BigDecimal,
    val date: LocalDateTime,
    val category: Category,
    val description: String?
) : Serializable
