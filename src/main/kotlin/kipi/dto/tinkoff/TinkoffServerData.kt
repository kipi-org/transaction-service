package kipi.dto.tinkoff

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "SONRS")
data class TinkoffServerData(
    @JacksonXmlProperty(localName = "STATUS")
    val status: TinkoffServerResponseStatus,
    @JacksonXmlProperty(localName = "DTSERVER")
    var dtServer: String,
    @JacksonXmlProperty(localName = "LANGUAGE")
    val language: String
)
