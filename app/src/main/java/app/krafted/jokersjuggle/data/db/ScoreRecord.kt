package app.krafted.jokersjuggle.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scores")
data class ScoreRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val score: Int,
    val timeSurvivedSeconds: Int,
    val maxObjectsReached: Int,
    val timestamp: Long = System.currentTimeMillis()
)
