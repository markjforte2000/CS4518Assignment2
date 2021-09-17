package wpi.mjforte.cs4518assignment2

import androidx.lifecycle.ViewModel
import kotlin.random.Random

class GameListViewModel : ViewModel() {

    val games = mutableListOf<BasketballGame>()

    init {
        for (i in 0 until 100) {
            val teamAScore = Random.nextInt(99)
            val teamBScore = Random.nextInt(99)
            val teamAName = "Game $i Team A"
            val teamBName = "Game $i Team B"
            val game = BasketballGame(teamAScore, teamBScore, teamAName, teamBName, i)
            games += game
        }
    }

}