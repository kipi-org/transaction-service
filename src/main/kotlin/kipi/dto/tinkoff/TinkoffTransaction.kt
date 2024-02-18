package kipi.dto.tinkoff

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class TinkoffTransaction(
    @JacksonXmlProperty(localName = "FITID")
    val id: String,
    @JacksonXmlProperty(localName = "TRNTYPE")
    val transactionType: String,
    @JacksonXmlProperty(localName = "DTPOSTED")
    val date: String,
    @JacksonXmlProperty(localName = "TRNAMT")
    val amount: String,
    @JacksonXmlProperty(localName = "NAME")
    val name: String,
    @JacksonXmlProperty(localName = "MEMO")
    val memo: String,
    @JacksonXmlProperty(localName = "CURRENCY")
    val tinkoffCurrency: TinkoffCurrency,
)
