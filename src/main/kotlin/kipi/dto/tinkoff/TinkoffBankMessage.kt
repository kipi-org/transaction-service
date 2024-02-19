package kipi.dto.tinkoff

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "STMTTRNRS")
data class TinkoffBankMessage(
    @JacksonXmlProperty(localName = "TRNUID")
    val trnuId: String?,
    @JacksonXmlProperty(localName = "STATUS")
    val tinkoffServerResponseStatus: TinkoffServerResponseStatus?,
    @JacksonXmlProperty(localName = "STMTRS")
    val tinkoffTransactionsWithAccount: TinkoffTransactionsWithAccount?
)
