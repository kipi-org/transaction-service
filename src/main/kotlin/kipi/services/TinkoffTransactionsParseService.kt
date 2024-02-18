package kipi.services

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import kipi.dto.TransactionDraft
import kipi.dto.tinkoff.TinkoffStatement
import kipi.dto.tinkoff.TinkoffXmlRequest
import kipi.utils.TinkoffParseUtils.parseTinkoffDate

class TinkoffTransactionsParseService(
    private val categoryService: CategoryService
) {
    companion object {
        private const val DEFAULT_CATEGORY_NAME = "Другое"
    }

    fun parseTransactions(userId: Long, tinkoffXmlRequest: TinkoffXmlRequest): List<TransactionDraft> =
        tinkoffTransactionToTransaction(userId, parseTinkoffTransactions(tinkoffXmlRequest.transactionsXml))

    private fun tinkoffTransactionToTransaction(
        userId: Long,
        tinkoffStatement: TinkoffStatement
    ): List<TransactionDraft> {
        val tinkoffTransactionsWithAccount =
            tinkoffStatement.tinkoffBankMessageWrapper.tinkoffBankMessage.tinkoffTransactionsWithAccount
        val categories = categoryService.findCategories(userId)

        return tinkoffTransactionsWithAccount.tinkoffTransactionsPack.tinkoffTransactions.map {
            TransactionDraft(
                foreignId = it.id,
                amount = it.amount.toBigDecimal(),
                description = it.name,
                categoryId = categories.find { category -> category.name.lowercase() == it.memo }?.id
                    ?: categories.find { category -> category.name == DEFAULT_CATEGORY_NAME }!!.id,
                date = parseTinkoffDate(it.date)
            )
        }
    }

    private fun parseTinkoffTransactions(transactionsXml: String): TinkoffStatement {
        val xmlMapper = XmlMapper()
        return try {
            xmlMapper.readValue(transactionsXml, TinkoffStatement::class.java)
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
    }
}