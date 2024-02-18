package kipi.dto

import java.io.Serializable

data class TransactionUpdates(
    val categoryId: Long?,
    val description: String?
) : Serializable