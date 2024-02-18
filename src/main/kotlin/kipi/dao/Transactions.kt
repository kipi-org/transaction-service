package kipi.dao

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object Transactions : Table("transactions") {
    val id = long("id").autoIncrement("transaction_id_seq")
    val accountId = long("accountId")
    val txTypeId = reference("txTypeId", TransactionTypes.id)
    val categoryId = reference("categoryId", Categories.id)
    val amount = decimal("amount", 18, 2)
    val date = datetime("date")
    val description = varchar("description", 255).nullable()
    val foreignId = varchar("foreignId", 255).nullable()
    override val primaryKey = PrimaryKey(id)
}