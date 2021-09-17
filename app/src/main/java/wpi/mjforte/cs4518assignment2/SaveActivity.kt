package wpi.mjforte.cs4518assignment2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val EXTRA_TEAM_A_SCORE = "wpi.mjforte.cs4518assignment2.teamAScore"
private const val EXTRA_TEAM_B_SCORE = "wpi.mjforte.cs4518assignment2.teamBScore"
private const val EXTRA_DID_SAVE = "wpi.mjforte.cs4518assignment2.didSave"

const val SAVE_ACTIVITY_CODE = 111

class SaveActivity : AppCompatActivity() {


    companion object {
        fun newIntent(packageContext: Context, teamAScore: Int, teamBScore: Int): Intent {
            return Intent(packageContext, SaveActivity::class.java).apply {
                putExtra(EXTRA_TEAM_A_SCORE, teamAScore)
                putExtra(EXTRA_TEAM_B_SCORE, teamBScore)
            }
        }
        fun getDidSaveResult(intent: Intent): Boolean {
            return intent.getBooleanExtra(EXTRA_DID_SAVE, false)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("WPIActivitySave", "Init app")
        setContentView(R.layout.activity_save)
        val teamAScore = intent.getIntExtra(EXTRA_TEAM_A_SCORE, 0)
        val teamBScore = intent.getIntExtra(EXTRA_TEAM_B_SCORE, 0)
        Log.d("WPIActivitySave","Got scores : A:$teamAScore, B:$teamBScore")
        findViewById<Button>(R.id.buttonSave2).setOnClickListener {
            it.isEnabled = false
            setResult(SAVE_ACTIVITY_CODE, Intent().apply {
                putExtra(EXTRA_DID_SAVE, true)
            })
        }
        findViewById<TextView>(R.id.teamAScore).text = "Team A Score: $teamAScore"
        findViewById<TextView>(R.id.teamBScore).text = "Team B Score: $teamBScore"

    }

}