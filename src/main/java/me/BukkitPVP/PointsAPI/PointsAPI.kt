package me.BukkitPVP.PointsAPI

import net.groundmc.pointsapi.PointsTable
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

object PointsAPI {

    lateinit var pointsTable: PointsTable

    fun addPoints(offlinePlayer: OfflinePlayer, points: Long) {
        pointsTable.addPoints(offlinePlayer.uniqueId, points)
    }

    fun removePoints(player: Player, points: Long) {
        pointsTable.removePoints(player.uniqueId, points)
    }

    fun removePoints(offlinePlayer: OfflinePlayer, points: Long) {
        pointsTable.removePoints(offlinePlayer.uniqueId, points)
    }

    fun setPoints(player: Player, points: Long) {
        pointsTable.setPoints(player.uniqueId, points)
    }

    fun setPoints(offlinePlayer: OfflinePlayer, points: Long) {
        pointsTable.setPoints(offlinePlayer.uniqueId, points)
    }

    fun getPoints(player: Player): Long {
        return pointsTable.getPoints(player.uniqueId)
    }

    fun getPoints(offlinePlayer: OfflinePlayer): Long {
        return pointsTable.getPoints(offlinePlayer.uniqueId)
    }

}
