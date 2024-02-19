package kipi.dto.tinkoff

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "BANKACCTFROM")
data class TinkoffAboutAccount(
    @JacksonXmlProperty(localName = "BANKID")
    val bankId: String?,
    @JacksonXmlProperty(localName = "ACCTID")
    val accountId: String?,
    @JacksonXmlProperty(localName = "ACCTTYPE")
    val accountType: String?
)
