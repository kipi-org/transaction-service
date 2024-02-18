package kipi.dto.tinkoff

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "BANKMSGSRSV1")
data class TinkoffBankMessageWrapper(
    @JacksonXmlProperty(localName = "STMTTRNRS")
    val tinkoffBankMessage: TinkoffBankMessage
)
