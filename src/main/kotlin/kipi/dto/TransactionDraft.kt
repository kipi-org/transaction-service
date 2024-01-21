package kipi.dto

import java.io.Serializable
import java.math.BigDecimal

data class TransactionDraft(
    val type: TransactionType,
    val amount: BigDecimal,
    val categoryId: Long,
    val description: String?
) : Serializable