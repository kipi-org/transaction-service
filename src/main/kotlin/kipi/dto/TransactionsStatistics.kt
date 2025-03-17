package kipi.dto

import java.math.BigDecimal

data class TransactionsStatistics(
    val incomeRemainder: BigDecimal,
    val incomeRemainderPercentage: BigDecimal?,
    val income: BigDecimal,
    val outcome: BigDecimal,
    val fns: BigDecimal?,
)
