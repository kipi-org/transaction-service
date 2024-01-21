package kipi.dto

import java.io.Serializable

data class Category(
    val id: Long,
    val userId: Long,
    val name: String,
    val iconUrl: String,
    val colorCode: String
) : Serializable
