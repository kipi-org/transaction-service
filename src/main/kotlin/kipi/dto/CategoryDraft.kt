package kipi.dto

import java.io.Serializable

data class CategoryDraft(
    val name: String,
    val iconUrl: String,
    val colorCode: String
) : Serializable