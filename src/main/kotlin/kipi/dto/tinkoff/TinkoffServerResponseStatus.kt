package kipi.dto.tinkoff

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "STATUS")
data class TinkoffServerResponseStatus(
    @JacksonXmlProperty(localName = "CODE")
    val code: String,
    @JacksonXmlProperty(localName = "SEVERITY")
    val severity: String
)
