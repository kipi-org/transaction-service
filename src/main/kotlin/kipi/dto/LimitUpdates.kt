package kipi.dto

import java.io.Serializable
import java.math.BigDecimal

data class LimitUpdates(
    val amount: BigDecimal
) : Serializable