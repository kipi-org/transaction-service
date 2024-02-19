package kipi.dto.tinkoff

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "SIGNONMSGSRSV1")
data class TinkoffServerDataWrapper(
    @JacksonXmlProperty(localName = "SONRS")
    val tinkoffServerData: TinkoffServerData?
)
