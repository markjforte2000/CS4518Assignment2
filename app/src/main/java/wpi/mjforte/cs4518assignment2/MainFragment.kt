package wpi.mjforte.cs4518assignment2

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import java.util.*

private val GAME_ID = "game_id"
private val TEAM_A_SCORE = "team_a_score"
private val TEAM_B_SCORE = "team_b_score"
private val TEAM_A_NAME = "team_a_name"
private val TEAM_B_NAME = "team_b_name"
private val GAME_DATE = "game_date"

class MainFragment : Fragment() {

    companion object {
        fun newBundle(game: BasketballGame): Bundle {
            val bundle = Bundle()
            bundle.putString(GAME_ID, game.id.toString())
            bundle.putInt(TEAM_A_SCORE, game.teamAScore)
            bundle.putInt(TEAM_B_SCORE, game.teamBScore)
            bundle.putString(TEAM_A_NAME, game.teamAName)
            bundle.putString(TEAM_B_NAME, game.teamBName)
            bundle.putLong(GAME_DATE, game.date.time)
            return bundle
        }
        fun getGame(bundle: Bundle): BasketballGame {
            return BasketballGame(
                UUID.fromString(bundle.getString(GAME_ID)),
                bundle.getInt(TEAM_A_SCORE), bundle.getInt(TEAM_B_SCORE),
                bundle.getString(TEAM_A_NAME) ?: "Team A",
                bundle.getString(TEAM_B_NAME) ?: "Team B",
                Date(bundle.getLong(GAME_DATE))
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("WPIFragmentMain","Created app")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val model : BasketballState by viewModels()
        if (arguments != null) {
            model.loadGame(getGame(arguments!!))
        }
        val teamAName = view.findViewById<EditText>(R.id.labelTeamA)
        teamAName.setText(model.getNameTeamA())
        val teamBName = view.findViewById<EditText>(R.id.labelTeamB)
        teamBName.setText(model.getNameTeamB())
        teamAName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.d(TAG, "Set Team A Name To ${p0.toString()}")
                model.setNameTeamA(p0.toString())
            }
        })
        teamBName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.d(TAG, "Set Team B Name To ${p0.toString()}")
                model.setNameTeamB(p0.toString())
            }
        })
        BasketballController(view, model)
        view.findViewById<Button>(R.id.buttonSave).setOnClickListener {
            saveGame(model)
        }
        view.findViewById<Button>(R.id.buttonDisplay).setOnClickListener {
            saveGame(model)
            val winningTeam = if (model.getScoreTeamA() > model.getScoreTeamB()) {
                model.getNameTeamA()
            } else if (model.getNameTeamB() > model.getNameTeamA()) {
                model.getNameTeamB()
            } else {
                ""
            }
            Log.d(TAG, "Starting list display with winning team $winningTeam")
            val fragment = GameListFragment()
            val args = GameListFragment.newBundle(winningTeam)
            fragment.arguments = args
            val fg = activity!!.supportFragmentManager
            fg.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
        return view
    }

    private fun saveGame(model: BasketballState) {
        Thread {
            val game = model.toGame()
            Log.d(TAG, "Saving game ${game.id}")
            BasketballRepository.get().saveGame(game)
            Log.d(TAG, "Saved game ${game.id}")
        }.start()
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("WPIFragmentMain", "Received results from Intent")
        if (requestCode == SAVE_ACTIVITY_CODE) {
            Log.d("WPIFragmentMain","Received data from save activity")
            val didSave = data?.let { SaveActivity.getDidSaveResult(it) }
            if (didSave == true) {
                Log.d("WPIFragmentMain", "ActivitySave was saved")
                val toast = Toast.makeText(context, "Data Saved", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

}