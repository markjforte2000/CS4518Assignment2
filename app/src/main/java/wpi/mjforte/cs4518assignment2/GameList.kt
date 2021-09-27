package wpi.mjforte.cs4518assignment2

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class GameListViewModel : ViewModel() {

    private val gameRepository = BasketballRepository.get()
    val gameListLiveData = gameRepository.getGames()


}