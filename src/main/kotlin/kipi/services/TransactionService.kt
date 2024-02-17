package kipi.services

import kipi.dto.*
import kipi.dto.GapType.*
import kipi.exceptions.CategoryException
import kipi.repositories.TransactionRepository
import java.math.BigDecimal.ZERO
import java.time.DayOfWeek
import java.time.LocalDate.now
import java.time.LocalDateTime

class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val categoryService: CategoryService
) {
    fun createTransaction(userId: Long, accountId: Long, transactionDraft: TransactionDraft): Long {
        val categories = categoryService.findCategories(userId)
        if (categories.none { it.id == transactionDraft.categoryId }) throw CategoryException("category.not.exist")

        return transactionRepository.createTransaction(accountId, transactionDraft)
    }

    fun getTransactions(
        accountIds: List<Long>,
        from: LocalDateTime? = null,
        to: LocalDateTime? = null,
        page: Int = 0,
        pageSize: Int = 15
    ) = transactionRepository.findTransactions(accountIds, from, to, page, pageSize)

    fun getTransactionsGaps(
        accountIds: List<Long>,
        gapType: GapType,
        page: Int = 0,
        pageSize: Int = 15,
    ): List<TransactionGap> {
        val skip = page.toLong() * pageSize.toLong()
        var currentDay = now().atStartOfDay()
        val periods = ArrayList<Period>()
        when (gapType) {
            DAY -> {
                currentDay = currentDay.minusDays(skip)
                for (i in 0 until pageSize) {
                    periods.add(
                        Period(
                            from = currentDay,
                            to = currentDay.plusHours(23).plusMinutes(59).plusSeconds(59)
                        )
                    )
                    currentDay = currentDay.minusDays(1)
                }
            }

            WEEK -> {
                currentDay = currentDay.toLocalDate().with(DayOfWeek.MONDAY).atStartOfDay()
                currentDay = currentDay.minusWeeks(skip)
                for (i in 0 until pageSize) {
                    periods.add(
                        Period(
                            from = currentDay,
                            to = currentDay.plusDays(6).plusHours(23).plusMinutes(59).plusSeconds(59)
                        )
                    )
                    currentDay = currentDay.minusWeeks(1)
                }
            }

            MONTH -> {
                currentDay = currentDay.toLocalDate().withDayOfMonth(1).atStartOfDay()
                currentDay = currentDay.minusMonths(skip)
                for (i in 0 until pageSize) {
                    periods.add(
                        Period(
                            from = currentDay,
                            to = currentDay.plusMonths(1).minusSeconds(1)
                        )
                    )
                    currentDay = currentDay.minusMonths(1)
                }
            }

            YEAR -> {
                currentDay = currentDay.toLocalDate().withMonth(1).withDayOfMonth(1).atStartOfDay()
                currentDay = currentDay.minusYears(skip)
                for (i in 0 until pageSize) {
                    periods.add(
                        Period(
                            from = currentDay,
                            to = currentDay.plusYears(1).minusSeconds(1)
                        )
                    )
                    currentDay = currentDay.minusYears(1)
                }
            }
        }

        return periods.map {
            val transactions = transactionRepository.findTransactions(accountIds, it.from, it.to)
            val income =
                transactions.filter { tx -> tx.amount >= ZERO }.map { tx -> tx.amount }.reduceOrNull { am1, am2 ->
                    am1 + am2
                }
            val outcome =
                transactions.filter { tx -> tx.amount < ZERO }.map { tx -> tx.amount }.reduceOrNull { am1, am2 ->
                    am1 + am2
                }

            TransactionGap(gapType, it, Statement(income = income ?: ZERO, outcome = outcome ?: ZERO))
        }
    }

    fun deleteTransaction(id: Long) = transactionRepository.deleteTransaction(id)
}