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
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class MainFragment : Fragment() {

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