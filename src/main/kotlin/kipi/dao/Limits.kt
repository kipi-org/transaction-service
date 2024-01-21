package kipi.dao

import org.jetbrains.exposed.sql.Table

object Limits : Table("limits") {
    val id = long("id").autoIncrement("limits_id_seq")
    val amount = decimal("amount", 18, 2)
    val currentAmount = decimal("currentAmount", 18, 2)
    val categoryId = reference("categoryId", Categories.id)
    override val primaryKey = PrimaryKey(id)
}