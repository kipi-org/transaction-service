package kipi.dto.tinkoff

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "OFX")
data class TinkoffStatement(
    @JacksonXmlProperty(localName = "SIGNONMSGSRSV1")
    val tinkoffServerDataWrapper: TinkoffServerDataWrapper,
    @JacksonXmlProperty(localName = "BANKMSGSRSV1")
    val tinkoffBankMessageWrapper: TinkoffBankMessageWrapper
)
