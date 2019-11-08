package net.groundmc.pointsapi

import me.BukkitPVP.PointsAPI.PointsAPI
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission
import java.util.*

class PointsAPICommand : TabExecutor {

    private val adminPermission: Permission = Bukkit.getPluginManager().getPermission("pointsapi.admin")

    override fun onCommand(sender: CommandSender, command: Command?, label: String?, args: Array<out String>?): Boolean {
        if (!sender.hasPermission(adminPermission)) {
            return true
        }
        if (args != null && args.isNotEmpty()) {
            if (args.size >= 2) {
                val player = try {
                    Bukkit.getOfflinePlayer(UUID.fromString(args[1]))
                } catch (exception: IllegalArgumentException) {
                    Bukkit.getPlayer(args[1])
                } ?: run {
                    sender.sendMessage("${args[1]} is no valid UUID or name of a player that is currently online.")
                    sender.sendMessage("Can't get the UUID of the player.")
                    return false
                }
                when (args.size) {
                    2 -> when (args[0]) {
                        "get" -> sender.sendMessage("Points of ${player.name}: ${PointsAPI.getPoints(player)}")
                        else -> return false
                    }
                    3 -> {
                        val points = args[2].toLong()
                        when (args[0]) {
                            "add" -> PointsAPI.addPoints(player, points)
                            "remove" -> PointsAPI.removePoints(player, points)
                            "set" -> PointsAPI.setPoints(player, points)
                            else -> return false
                        }
                        sender.sendMessage("Points of ${player.name}: ${PointsAPI.getPoints(player)}")
                    }
                    else -> return false
                }
            }
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command?, alias: String?, args: Array<out String>?): List<String> {
        if (args != null && args.isNotEmpty()) {
            when (args.size) {
                0, 1 -> return listOf("get", "add", "remove", "set").filter { it.startsWith(args[0]) }
                2 -> return Bukkit.matchPlayer(args[0]).map(Player::getName)
            }
        }
        return emptyList()
    }

}
