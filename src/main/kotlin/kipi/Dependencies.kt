package kipi

import kipi.controllers.*
import kipi.repositories.CategoryRepository
import kipi.repositories.LimitRepository
import kipi.repositories.TransactionRepository
import kipi.services.*

class Dependencies {
    val config = Config()

    private val transactionRepository = TransactionRepository()
    private val categoryRepository = CategoryRepository()
    private val limitRepository = LimitRepository()
    private val categoryService = CategoryService(categoryRepository, config)
    private val transactionService = TransactionService(transactionRepository, categoryService)
    private val limitService = LimitService(limitRepository, categoryService, transactionService)
    private val statisticsService = StatisticsService(transactionService)
    private val tinkoffTransactionsParseService = TinkoffTransactionsParseService(categoryService)
    val transactionCreateController = TransactionCreateController(transactionService)
    val transactionFetchController = TransactionFetchController(transactionService)
    val categoryCreateController = CategoryCreateController(categoryService)
    val categoriesFindController = CategoriesFindController(categoryService)
    val categoryDeleteController = CategoryDeleteController(categoryService)
    val limitCreateController = LimitCreateController(limitService)
    val limitsFindController = LimitsFindController(limitService)
    val limitDeleteController = LimitDeleteController(limitService)
    val transactionDeleteController = TransactionDeleteController(transactionService)
    val oneTransactionFindController = OneTransactionFindController(transactionService)
    val gapFetchController = GapFetchController(transactionService)
    val categoriesStatisticsController = CategoriesStatisticsController(statisticsService)
    val createBaseCategoriesController = CreateBaseCategoriesController(categoryService)
    val transactionUpdateController = TransactionUpdateController(transactionService, categoryService)
    val tinkoffTransactionsParseController =
        TinkoffTransactionsParseController(tinkoffTransactionsParseService, transactionService)
    val deleteUserInfoController = DeleteUserInfoController(transactionService, limitService, categoryService)
    val updateLimitController = UpdateLimitController(limitService)
}