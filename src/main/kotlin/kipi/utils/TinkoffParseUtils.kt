package kipi.utils

import java.time.LocalDateTime

object TinkoffParseUtils {
    fun parseTinkoffDate(dateDraft: String): LocalDateTime {
        val year = dateDraft.substring(0, 4).toInt()
        val month = dateDraft.substring(4, 6).toInt()
        val day = dateDraft.substring(6, 8).toInt()
        val hour = dateDraft.substring(8, 10).toInt()
        val min = dateDraft.substring(10, 12).toInt()
        val seconds = dateDraft.substring(12, 14).toInt()

        return LocalDateTime.of(year, month, day, hour, min, seconds)
    }
}