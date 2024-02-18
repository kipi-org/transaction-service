package kipi.dto

import java.io.Serializable

data class DefaultCategoryDraft(
    val name: String,
    val iconUrl: String
) : Serializable