package wpi.mjforte.cs4518assignment2

import java.util.*

class BasketballGame(teamAScore: Int, teamBScore: Int, teamAName: String, teamBName: String, id: Int, ) {

    val teamAScore: Int = teamAScore
    val teamBScore: Int = teamBScore
    val teamAName: String = teamAName
    val teamBName: String = teamBName
    val id: Int = id
    val time: Date = Calendar.getInstance().time

}