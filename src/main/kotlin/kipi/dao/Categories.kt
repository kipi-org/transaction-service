package kipi.dao

import org.jetbrains.exposed.sql.Table

object Categories : Table("categories") {
    val id = long("id").autoIncrement("category_id_seq")
    val userId = long("userId")
    val name = varchar("name", 255)
    val iconUrl = varchar("iconUrl", 255)
    val colorCode = varchar("colorCode", 255)
    override val primaryKey = PrimaryKey(id)
}