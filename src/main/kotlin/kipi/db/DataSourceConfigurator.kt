package kipi.db

import org.postgresql.ds.PGSimpleDataSource
import javax.sql.DataSource

class DataSourceConfigurator {
    fun createDataSource(): DataSource {
        val dataSource = PGSimpleDataSource()
        dataSource.serverNames = arrayOf("localhost")
        dataSource.databaseName = "postgres"
        dataSource.user = "postgres"
        dataSource.password = "6623174119Zz!"

        return dataSource
    }
}