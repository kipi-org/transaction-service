package kipi.services

import kipi.dto.*
import kipi.dto.GapType.*
import kipi.exceptions.CategoryException
import kipi.repositories.TransactionRepository
import java.math.BigDecimal
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

    fun createManyTransactionsWithForeignId(accountId: Long, transactionDrafts: List<TransactionDraft>) {
        val transactionsForeignIds =
            transactionRepository.findTransactions(listOf(accountId)).mapNotNull { it.foreignId }
        val transactionsForSave = transactionDrafts.filterNot { it.foreignId in transactionsForeignIds }

        if (transactionsForSave.isEmpty()) return

        transactionsForSave.chunked(500).forEach {
            transactionRepository.createManyTransactions(accountId, it)
        }
    }

    fun getTransactions(
        accountIds: List<Long>,
        from: LocalDateTime? = null,
        to: LocalDateTime? = null,
        page: Int = 0,
        pageSize: Int = 15,
        categoryId: Long? = null
    ) = transactionRepository.findTransactions(accountIds, from, to, page, pageSize, categoryId)


    fun getTransactionsStatistics(
        accountIds: List<Long>
    ): TransactionsStatistics {
        val now = LocalDateTime.now()
        val allTransactions = transactionRepository.findTransactions(accountIds)
        val allAmount = allTransactions.map { it.amount }.reduceOrNull { am1, am2 ->
            am1 + am2
        } ?: ZERO
        val avgMonthIncome = allTransactions.filter { tx -> tx.amount >= ZERO }
            .groupBy { it.date.year.toString() + it.date.month.toString() }
            .map { it.value }.map { it.sumOf { el -> el.amount } }
            .let { it.sumOf { el -> el } / it.size.toBigDecimal() }
        val avgMonthOutcome = allTransactions.filter { tx -> tx.amount < ZERO }
            .groupBy { it.date.year.toString() + it.date.month.toString() }
            .map { it.value }.map { it.sumOf { el -> el.amount } }
            .let { it.sumOf { el -> el } / it.size.toBigDecimal() }.abs()
        val transactions = transactionRepository.findTransactions(accountIds, now.minusDays(30), now)
        val income =
            transactions.filter { tx -> tx.amount >= ZERO }.map { tx -> tx.amount }.reduceOrNull { am1, am2 ->
                am1 + am2
            } ?: ZERO
        val outcome =
            transactions.filter { tx -> tx.amount < ZERO }.map { tx -> tx.amount }.reduceOrNull { am1, am2 ->
                am1 + am2
            } ?: ZERO
        val incomeRemainder = income - outcome
        val incomeRemainderPercentage = (incomeRemainder / income) * BigDecimal(100)
        val savingRatioPercentage = (avgMonthIncome / allAmount) * 100.toBigDecimal()
        val savingRatio = when {
            savingRatioPercentage < BigDecimal(5) -> 0.toBigDecimal()
            savingRatioPercentage < BigDecimal(20) -> 20.toBigDecimal()
            else -> 40.toBigDecimal()
        }
        val liquidityRiskInMonths = allAmount / avgMonthOutcome
        val liquidityRisk = when {
            liquidityRiskInMonths < BigDecimal(1) -> 0.toBigDecimal()
            liquidityRiskInMonths < BigDecimal(6) -> 15.toBigDecimal()
            else -> 30.toBigDecimal()
        }

        val expenseControlPercentage = (avgMonthOutcome / avgMonthIncome) * 100.toBigDecimal()
        val expenseControl = when {
            expenseControlPercentage > BigDecimal(100) -> 0.toBigDecimal()
            savingRatioPercentage > BigDecimal(70) -> 15.toBigDecimal()
            else -> 30.toBigDecimal()
        }

        return TransactionsStatistics(
            incomeRemainder = incomeRemainder,
            incomeRemainderPercentage = incomeRemainderPercentage,
            income = income,
            outcome = outcome,
            fns = savingRatio + liquidityRisk + expenseControl,
        )
    }


    fun getTransactionsGaps(
        accountIds: List<Long>,
        gapType: GapType,
        page: Int = 0,
        pageSize: Int = 15,
        categoryId: Long? = null
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
            val transactions =
                transactionRepository.findTransactions(accountIds, it.from, it.to, categoryId = categoryId)
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

    fun deleteTransactions(accountsIds: List<Long>) =
        transactionRepository.deleteTransactionsWithAccountsIds(accountsIds)

    fun findTransaction(id: Long) = transactionRepository.findTransaction(id)

    fun updateTransaction(transactionId: Long, updates: TransactionUpdates) =
        transactionRepository.updateTransaction(transactionId, updates)
}