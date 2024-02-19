package kipi.dto.tinkoff

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "BANKTRANLIST")
data class TinkoffTransactionsPack(
    @JacksonXmlProperty(localName = "DTSTART")
    val start: String,
    @JacksonXmlProperty(localName = "DTEND")
    val end: String,
    @JacksonXmlProperty(localName = "STMTTRN")
    @JacksonXmlCData
    @JacksonXmlElementWrapper(localName = "STMTTRN", useWrapping = false)
    val tinkoffTransactions: List<TinkoffTransaction> = emptyList()
)
