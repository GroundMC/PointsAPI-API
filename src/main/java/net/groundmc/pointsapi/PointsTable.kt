package net.groundmc.pointsapi

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class PointsTable(private val database: Database) : Table("Points") {
    private val id = uuid("player").primaryKey()
    private val points = long("points").index()

    fun addPoints(uuid: UUID, p: Long) = setPoints(uuid, getPoints(uuid) + p)

    private fun exists(uuid: UUID) = transaction(database) {
        select { id eq uuid }.count() > 0
    }

    fun getPoints(uuid: UUID) = transaction(database) {
        select { id eq uuid }.firstOrNull()?.get(points) ?: 0
    }

    fun removePoints(uuid: UUID, p: Long) = addPoints(uuid, -p)

    fun setPoints(uuid: UUID, p: Long) = transaction(database) {
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
