package net.groundmc.pointsapi

import com.zaxxer.hikari.HikariDataSource
import me.BukkitPVP.PointsAPI.PointsAPI
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class PointsAPIPlugin : JavaPlugin() {

    override fun onEnable() {
        Database.connect(HikariDataSource().apply {
            jdbcUrl = config.getString("database.url")
                    .replace("\$dataFolder", dataFolder.absolutePath)
            username = config.getString("database.username", "")
            password = config.getString("database.password", "")
            transactionIsolation = "TRANSACTION_READ_COMMITTED"
        })
        transaction {
            SchemaUtils.createMissingTablesAndColumns(PointsTable)
        }

        registerCommand()
    }

    fun registerCommand() {
        val paCommand = PointsApiCommand()
        getCommand("pa").apply {
            executor = paCommand
            tabCompleter = paCommand
        }
    }

    class PointsApiCommand : CommandExecutor, TabCompleter {

        private val adminPermission: Permission = Bukkit.getPluginManager().getPermission("pointsapi.admin")

        override fun onCommand(sender: CommandSender, command: Command?, label: String?, args: Array<out String>?): Boolean {
            if (!sender.hasPermission(adminPermission)) {
                return true
            }
            if (args != null && args.isNotEmpty()) {
                if (args.size >= 2) {
                    val player = Bukkit.getOfflinePlayer(
                            Bukkit.createProfile(args[1]).apply {
                                complete(false)
                            }.id
                    )
                    if (args.size == 2) {
                        when (args[0]) {
                            "get" -> sender.sendMessage("Points of $args[1]: ${PointsAPI.getPoints(player)}")
                        }
                    } else if (args.size == 3) {
                        val points = args[2].toLong()
                        when (args[0]) {
                            "add" -> PointsAPI.addPoints(player, points)
                            "remove" -> PointsAPI.removePoints(player, points)
                            "set" -> PointsAPI.setPoints(player, points)
                        }
                        sender.sendMessage("Points of $args[1]: ${PointsAPI.getPoints(player)}")
                    }
                }
            }
            return true
        }

        override fun onTabComplete(sender: CommandSender, command: Command?, alias: String?, args: Array<out String>?): List<String> {
            if (args != null && args.isNotEmpty()) {
                when (args.size) {
                    1 -> return listOf("get", "add", "remove", "set")
                    2 -> return Bukkit.matchPlayer(args[1]).map(Player::getName)
                }
            }
            return emptyList()
        }

    }
}

object PointsTable : Table("Points") {
    private val id = uuid("player").primaryKey()
    private val points = long("points").index()

    fun addPoints(uuid: UUID, p: Long) = setPoints(uuid, getPoints(uuid) + p)

    private fun exists(uuid: UUID) = transaction {
        select { id eq uuid }.count() > 0
    }

    fun getPoints(uuid: UUID) = transaction {
        select { id eq uuid }.firstOrNull()?.get(points) ?: 0
    }

    fun removePoints(uuid: UUID, p: Long) = addPoints(uuid, -p)

    fun setPoints(uuid: UUID, p: Long) = transaction {
        if (!exists(uuid)) {
            insert {
                it[id] = uuid
                it[points] = p
            }
        } else {
            update({ id eq uuid }) {
                it[points] = p
            }
        }
        return@transaction
    }
}
