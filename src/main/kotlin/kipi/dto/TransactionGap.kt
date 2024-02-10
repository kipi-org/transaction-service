package kipi.dto

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.LocalDateTime

data class TransactionGap(
    val type: GapType,
    val period: Period,
    val statement: Statement
)

data class Period(
    val from: LocalDateTime,
    val to: LocalDateTime
)

data class Statement(
    val income: BigDecimal = ZERO,
    val outcome: BigDecimal = ZERO
)