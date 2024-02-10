package kipi.dto

import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionDraft(
    val type: TransactionType,
    val amount: BigDecimal,
    val date: LocalDateTime?,
    val categoryId: Long,
    val description: String?
) : Serializable