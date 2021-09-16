package wpi.mjforte.cs4518assignment2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private lateinit var controller: BasketballController

    val TEAM_A = "team_a"
    val TEAM_B = "team_b"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("WPIActivityMain", "Init app")
        setContentView(R.layout.activity_main)
        // create controller

        val model : BasketballState by viewModels()

        controller = BasketballController(this, model)

        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            val intent = SaveActivity.newIntent(this, model.getScoreTeamA(), model.getScoreTeamB())
            Log.d("WPIActivityMain", "Starting save activity")
            startActivityForResult(intent, SAVE_ACTIVITY_CODE)
        }

        Log.d("WPIActivityMain","Created app")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("WPIActivityMain", "Received results from Intent")
        if (requestCode == SAVE_ACTIVITY_CODE) {
            Log.d("WPIActivityMain","Received data from save activity")
            val didSave = data?.let { SaveActivity.getDidSaveResult(it) }
            if (didSave == true) {
                Log.d("WPIActivityMain", "ActivitySave was saved")
                val toast = Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

}

