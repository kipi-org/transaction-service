package kipi.dto.tinkoff

data class TinkoffXmlRequest(
    val accountId: Long,
    val transactionsXml: String
)
