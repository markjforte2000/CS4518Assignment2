package wpi.mjforte.cs4518assignment2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels

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
        BasketballController(view, model)
        view.findViewById<Button>(R.id.buttonSave).setOnClickListener {
            val intent =
                context?.let { it1 -> SaveActivity.newIntent(it1, model.getScoreTeamA(), model.getScoreTeamB()) }
            Log.d("WPIFragmentMain", "Starting save activity")
            startActivityForResult(intent, SAVE_ACTIVITY_CODE)
        }
        return view
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