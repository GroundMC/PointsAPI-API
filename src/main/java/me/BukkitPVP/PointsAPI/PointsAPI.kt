package me.BukkitPVP.PointsAPI

import net.groundmc.pointsapi.PointsTable
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

object PointsAPI {
    fun addPoints(offlinePlayer: OfflinePlayer, points: Int) {
        PointsTable.addPoints(offlinePlayer.uniqueId, points.toLong())
    }

    fun removePoints(player: Player, points: Int) {
        PointsTable.removePoints(player.uniqueId, points.toLong())
    }

    fun removePoints(offlinePlayer: OfflinePlayer, points: Int) {
        PointsTable.removePoints(offlinePlayer.uniqueId, points.toLong())
    }

    fun setPoints(player: Player, points: Int) {
        PointsTable.setPoints(player.uniqueId, points.toLong())
    }

    fun setPoints(offlinePlayer: OfflinePlayer, points: Int) {
        PointsTable.setPoints(offlinePlayer.uniqueId, points.toLong())
    }

    fun getPoints(player: Player): Long {
        return PointsTable.getPoints(player.uniqueId)
    }

    fun getPoints(offlinePlayer: OfflinePlayer): Long {
        return PointsTable.getPoints(offlinePlayer.uniqueId)
    }

}
