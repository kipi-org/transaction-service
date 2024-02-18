package kipi.dto

import kipi.dto.TransactionType.BASE
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionDraft(
    val type: TransactionType = BASE,
    val amount: BigDecimal,
    val date: LocalDateTime?,
    val categoryId: Long,
    val description: String?,
    val foreignId: String? = null
) : Serializable