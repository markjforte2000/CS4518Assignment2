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

    @Query("UPDATE table_game SET teamAName=:teamAName, teamBName=:teamBName, teamAScore=:teamAScore, teamBScore=:teamBScore, date=:date WHERE id=:id")
    fun updateGame(id: UUID, teamAScore: Int, teamBScore: Int, teamAName: String, teamBName: String, date: Date)

    @Query("SELECT EXISTS(SELECT 1 FROM table_game WHERE id=:id)")
    fun doesGameExist(id: UUID): Boolean

    @Query("SELECT * FROM table_game WHERE teamAName=:teamName OR teamBName=:teamName")
    fun getGames(teamName: String): LiveData<List<BasketballGame>>

}