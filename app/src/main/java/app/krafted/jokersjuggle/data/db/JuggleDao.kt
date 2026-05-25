package app.krafted.jokersjuggle.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface JuggleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(record: ScoreRecord)

    @Query("SELECT * FROM scores ORDER BY score DESC, timestamp ASC LIMIT 10")
    suspend fun getTopScores(): List<ScoreRecord>

    @Query("SELECT MAX(score) FROM scores")
    suspend fun getBestScore(): Int?

    @Query("SELECT MAX(maxObjectsReached) FROM scores")
    suspend fun getMaxObjectsReached(): Int?

    @Query("SELECT MAX(timeSurvivedSeconds) FROM scores")
    suspend fun getLongestSurvivalTime(): Int?
}
