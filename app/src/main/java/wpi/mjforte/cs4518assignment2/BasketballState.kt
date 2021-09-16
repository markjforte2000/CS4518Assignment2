package wpi.mjforte.cs4518assignment2

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BasketballState : ViewModel() {

    private val scoreTeamA: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    private val scoreTeamB: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    init {
        Log.d("BasketballViewModel", "Init ViewModel")
        reset()
    }

    fun reset() {
        scoreTeamA.value = 0
        scoreTeamB.value = 0
    }

    fun incrementTeamAScore(amount: Int) {
        Log.d("BasketballViewModel", "Increasing team A score by $amount")
        scoreTeamA.value = scoreTeamA.value?.plus(amount)
    }

    fun incrementTeamBScore(amount: Int) {
        Log.d("BasketballViewModel", "Increasing team B score by $amount")
        scoreTeamB.value = scoreTeamB.value?.plus(amount)
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

}