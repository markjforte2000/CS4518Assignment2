package wpi.mjforte.cs4518assignment2

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.lang.IllegalStateException
import java.util.*

private const val DATABASE_NAME = "game-database"

class BasketballRepository private constructor(context: Context) {

    private val database : BasketballDatabase = Room.databaseBuilder(
        context.applicationContext,
        BasketballDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val basketballDAO = database.basketballDAO()

    fun getGames() : LiveData<List<BasketballGame>> {
        return basketballDAO.getGames()
    }

    fun getGame(id: UUID) : LiveData<BasketballGame?> {
        return basketballDAO.getGame(id)
    }

    fun saveGame(game: BasketballGame) {
        basketballDAO.saveGame(game.id, game.teamAScore, game.teamBScore, game.teamAName, game.teamBName, game.date)
    }

    companion object {
        private var INSTANCE: BasketballRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = BasketballRepository(context)
            }
        }

        fun get(): BasketballRepository {
            return INSTANCE ?:
            throw IllegalStateException("Basketball repository has not been initialized")
        }
    }

}