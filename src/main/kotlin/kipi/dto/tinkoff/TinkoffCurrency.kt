package kipi.dto.tinkoff

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "CURRENCY")
data class TinkoffCurrency(
    @JacksonXmlProperty(localName = "CURSYM")
    val value: String,
    @JacksonXmlProperty(localName = "CURRATE")
    val currate: String
)
