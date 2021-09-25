package wpi.mjforte.cs4518assignment2

import androidx.room.Entity
import androidx.room.PrimaryKey
import  java.util.*

@Entity(tableName = "table_game")
class BasketballGame(@PrimaryKey val id: UUID = UUID.randomUUID(),
                     val teamAScore: Int, val teamBScore: Int,
                     val teamAName: String, val teamBName: String,
                     val date: Date)
{

}