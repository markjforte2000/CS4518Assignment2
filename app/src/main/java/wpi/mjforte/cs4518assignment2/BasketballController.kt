package wpi.mjforte.cs4518assignment2

import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlin.math.acos

class BasketballController(activity: MainActivity, private val state: BasketballState) {

    val TEAM_A = "team_a"
    val TEAM_B = "team_b"

    data class TeamPointMapping(val team: String, val points: Int)

    private val teamALabel: TextView
    private val teamBLabel: TextView

    init {
        Log.d("BasketballController", "Init state with A: ${state.getScoreTeamA()}, B: ${state.getScoreTeamB()}")
        val buttonIDMap = mapOf(
            R.id.buttonTeamAFreeThrow to TeamPointMapping(TEAM_A, 1),
            R.id.buttonTeamATwoPoints to TeamPointMapping(TEAM_A, 2),
            R.id.buttonTeamAThreePoints to TeamPointMapping(TEAM_A, 3),
            R.id.buttonTeamBFreeThrow to TeamPointMapping(TEAM_B, 1),
            R.id.buttonTeamBTwoPoints to TeamPointMapping(TEAM_B, 2),
            R.id.buttonTeamBThreePoints to TeamPointMapping(TEAM_B, 3),
        )
        for (pair in buttonIDMap) {
            activity.findViewById<Button>(pair.key).setOnClickListener{
                handlePointButtonPress(pair.value.team, pair.value.points)
            }
        }
        activity.findViewById<Button>(R.id.buttonReset).setOnClickListener{
            handleResetButtonPress()
        }
        teamALabel = activity.findViewById(R.id.pointsTeamA)
        teamBLabel = activity.findViewById(R.id.pointsTeamB)
        updateActivity()
        Log.d("BasketballController", "Init Controller Done")
    }

    private fun handlePointButtonPress(team: String, points: Int) {
        Log.d("BasketballController", "Adding $points to $team")
        if (team == TEAM_A) {
            state.incrementTeamAScore(points)
        } else {
            state.incrementTeamBScore(points)
        }
        updateActivity()
    }

    private fun handleResetButtonPress() {
        state.reset()
        updateActivity()
        Log.d("BasketballController", "Resetting Teams Score")
    }


    private fun updateActivity() {
        teamALabel.text = state.getScoreTeamA().toString()
        teamBLabel.text = state.getScoreTeamB().toString()
    }

    fun getTeamAScore() : Int {
        return state.getScoreTeamA()
    }

    fun getTeamBScore() : Int {
        return state.getScoreTeamB()
    }

}