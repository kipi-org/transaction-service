package kipi.dao

import org.jetbrains.exposed.sql.Table

object TransactionTypes : Table("tx_types") {
    val id = long("id").autoIncrement("tx_type_id_seq")
    val name = varchar("name", 255)
    override val primaryKey = PrimaryKey(id)
}