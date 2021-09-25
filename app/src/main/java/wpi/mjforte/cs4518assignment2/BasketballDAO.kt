package wpi.mjforte.cs4518assignment2

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import java.util.*

@Dao
interface BasketballDAO {

    @Query("SELECT * FROM table_game")
    fun getGames(): LiveData<List<BasketballGame>>

    @Query("SELECT * FROM table_game WHERE id=(:id)")
    fun getGame(id: UUID): LiveData<BasketballGame?>

    @Query("INSERT INTO table_game (id, teamAName, teamBName, teamAScore, teamBScore, date) VALUES (:id, :teamAName, :teamBName, :teamAScore, :teamBScore, :date)")
    fun saveGame(id: UUID, teamAScore: Int, teamBScore: Int, teamAName: String, teamBName: String, date: Date)
}