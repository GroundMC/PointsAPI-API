package net.groundmc.pointsapi

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import me.BukkitPVP.PointsAPI.PointsAPI
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class PointsAPIPlugin : JavaPlugin() {

    private lateinit var database: Database
    private lateinit var dataSource: HikariDataSource
    private lateinit var pointsTable: PointsTable

    override fun onEnable() {
        val config = HikariConfig().apply {
            jdbcUrl = config.getString("database.url")
                    .replace("\$dataFolder", dataFolder.absolutePath)
            username = config.getString("database.username", "")
            password = config.getString("database.password", "")
            transactionIsolation = "TRANSACTION_READ_COMMITTED"
        }
        dataSource = HikariDataSource(config)
        database = Database.connect(dataSource)
        pointsTable = PointsTable(database)
        transaction(database) {
            SchemaUtils.createMissingTablesAndColumns(pointsTable)
        }

        registerCommand()
        PointsAPI.pointsTable = pointsTable
    }

    override fun onDisable() {
        dataSource.close()
    }

    private fun registerCommand() {
        val paCommand = PointsAPICommand()
        getCommand("pa").apply {
            executor = paCommand
            tabCompleter = paCommand
        }
    }

}
