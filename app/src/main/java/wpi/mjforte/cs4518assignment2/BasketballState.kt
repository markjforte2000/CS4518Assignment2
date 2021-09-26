package wpi.mjforte.cs4518assignment2

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class BasketballState : ViewModel() {

    private val id : MutableLiveData<UUID> by lazy {
        MutableLiveData<UUID>()
    }

    private val scoreTeamA: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    private val scoreTeamB: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    private val nameTeamA: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private val nameTeamB: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private val date: MutableLiveData<Date> by lazy {
        MutableLiveData<Date>()
    }

    init {
        Log.d("BasketballViewModel", "Init ViewModel")
        reset()
    }

    fun reset() {
        scoreTeamA.value = 0
        scoreTeamB.value = 0
        nameTeamA.value = "Team A"
        nameTeamB.value = "Team B"
        date.value = Calendar.getInstance().time
        id.value = UUID.randomUUID()
    }

    fun incrementTeamAScore(amount: Int) {
        Log.d("BasketballViewModel", "Increasing team A score by $amount")
        scoreTeamA.value = scoreTeamA.value?.plus(amount)
    }

    fun incrementTeamBScore(amount: Int) {
        Log.d("BasketballViewModel", "Increasing team B score by $amount")
        scoreTeamB.value = scoreTeamB.value?.plus(amount)
    }

    fun setNameTeamA(name: String) {
        nameTeamA.value = name
    }

    fun setNameTeamB(name: String) {
        nameTeamB.value = name
    }

    fun getNameTeamA(): String {
        return nameTeamA.value?: "Team A"
    }

    fun getNameTeamB(): String {
        return nameTeamB.value?: "Team B"
    }

    fun getScoreTeamA() : Int {
        if (scoreTeamA.value != null) {
            return scoreTeamA.value!!
        }
        return 0
    }

    fun getScoreTeamB() : Int {
        if (scoreTeamB.value != null) {
            return scoreTeamB.value!!
        }
        return 0
    }

    fun toGame() : BasketballGame {
        return BasketballGame(
        id.value?: UUID.randomUUID(),
            getScoreTeamA(), getScoreTeamB(),
            getNameTeamA(), getNameTeamB(),
            date.value?: Calendar.getInstance().time
        )
    }

    fun loadGame(game: BasketballGame) {
        id.value = game.id
        scoreTeamA.value = game.teamAScore
        scoreTeamB.value = game.teamBScore
        nameTeamA.value = game.teamAName
        nameTeamB.value = game.teamBName
        date.value = game.date
    }

}