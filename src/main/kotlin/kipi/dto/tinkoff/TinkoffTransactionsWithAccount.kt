package kipi.dto.tinkoff

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "STMTRS")
data class TinkoffTransactionsWithAccount(
    @JacksonXmlProperty(localName = "CURDEF")
    val currency: String,
    @JacksonXmlProperty(localName = "BANKACCTFROM")
    val tinkoffAboutAccount: TinkoffAboutAccount,
    @JacksonXmlProperty(localName = "BANKTRANLIST")
    val tinkoffTransactionsPack: TinkoffTransactionsPack
)
