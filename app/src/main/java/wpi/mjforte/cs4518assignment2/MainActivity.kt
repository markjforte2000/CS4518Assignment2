package wpi.mjforte.cs4518assignment2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("WPIActivityMain", "Init app")
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            // val fragment = GameListFragment()
            val fragment = MainFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }

        Log.d("WPIActivityMain","Created app")
    }


}

