package me.BukkitPVP.PointsAPI

import net.groundmc.pointsapi.PointsTable
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

object PointsAPI {
    fun addPoints(offlinePlayer: OfflinePlayer, points: Long) {
        PointsTable.addPoints(offlinePlayer.uniqueId, points)
    }

    fun removePoints(player: Player, points: Long) {
        PointsTable.removePoints(player.uniqueId, points)
    }

    fun removePoints(offlinePlayer: OfflinePlayer, points: Long) {
        PointsTable.removePoints(offlinePlayer.uniqueId, points)
    }

    fun setPoints(player: Player, points: Long) {
        PointsTable.setPoints(player.uniqueId, points)
    }

    fun setPoints(offlinePlayer: OfflinePlayer, points: Long) {
        PointsTable.setPoints(offlinePlayer.uniqueId, points)
    }

    fun getPoints(player: Player): Long {
        return PointsTable.getPoints(player.uniqueId)
    }

    fun getPoints(offlinePlayer: OfflinePlayer): Long {
        return PointsTable.getPoints(offlinePlayer.uniqueId)
    }

}
