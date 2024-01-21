package kipi.db

import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import javax.sql.DataSource

class DbMigration(private val dataSource: DataSource) {
    fun migrate() {
        dataSource.connection.use {
            Liquibase(
                "db/index.xml",
                ClassLoaderResourceAccessor(Thread.currentThread().contextClassLoader),
                JdbcConnection(it)
            ).update("")
        }
    }
}