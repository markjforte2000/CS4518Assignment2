package wpi.mjforte.cs4518assignment2

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.ExifInterface
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

private val GAME_ID = "game_id"
private val TEAM_A_SCORE = "team_a_score"
private val TEAM_B_SCORE = "team_b_score"
private val TEAM_A_NAME = "team_a_name"
private val TEAM_B_NAME = "team_b_name"
private val GAME_DATE = "game_date"

private val REQUEST_IMAGE_CAPTURE_TEAM_A = 765
private val REQUEST_IMAGE_CAPTURE_TEAM_B = 563

private val PERMISSIONS_REQUEST_ID = 849

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

    private lateinit var locationManager: LocationManager

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
        view.findViewById<Button>(R.id.cameraButtonTeamA).setOnClickListener {
            takePicture(view, REQUEST_IMAGE_CAPTURE_TEAM_A)
        }
        view.findViewById<Button>(R.id.cameraButtonTeamB).setOnClickListener {
            takePicture(view, REQUEST_IMAGE_CAPTURE_TEAM_B)
        }

        if (context!!.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ID)
        } else {
            setupLocation()
        }

        return view
    }

    private fun takePicture(view: View, id: Int) {
        Log.d(TAG, "Attempting to take picture for $id")
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {photoIntent ->
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                Log.d(TAG, "ERROR CREATING FILE: $ex")
                null
            }
            Log.d(TAG, "Saving image to ${photoFile?.absolutePath}")
            photoFile?.also {
                val photoURI = FileProvider.getUriForFile(
                    context!!,
                    "com.example.android.fileprovider",
                    it
                )
                Log.d(TAG, "$photoURI")
                photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(photoIntent, id)
            }
        }
    }

    private lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun saveGame(model: BasketballState) {
        Thread {
            val game = model.toGame()
            Log.d(TAG, "Saving game ${game.id}")
            BasketballRepository.get().saveGame(game)
            Log.d(TAG, "Saved game ${game.id}")
        }.start()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("WPIFragmentMain", "Received results from Intent: $resultCode")
        if (requestCode == SAVE_ACTIVITY_CODE) {
            Log.d("WPIFragmentMain","Received data from save activity")
            val didSave = data?.let { SaveActivity.getDidSaveResult(it) }
            if (didSave == true) {
                Log.d("WPIFragmentMain", "ActivitySave was saved")
                val toast = Toast.makeText(context, "Data Saved", Toast.LENGTH_SHORT)
                toast.show()
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE_TEAM_A) {
            Log.d(TAG, currentPhotoPath)
            view!!.findViewById<ImageView>(R.id.imageViewTeamA).setImageBitmap(getImage(currentPhotoPath))
            Toast.makeText(context, "Got Image", Toast.LENGTH_SHORT).show()
        } else if (requestCode == REQUEST_IMAGE_CAPTURE_TEAM_B) {
            view!!.findViewById<ImageView>(R.id.imageViewTeamB).setImageBitmap(getImage(currentPhotoPath))
            Toast.makeText(context, "Got Image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getImage(path: String): Bitmap {
        val exifInterface = ExifInterface(path)
        var rotation = 0.0f;
        val orientation = exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotation = 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> rotation = 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> rotation = 270f
        }
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        options.inSampleSize = 3
        val source = BitmapFactory.decodeFile(path)
        val matrix = Matrix()
        matrix.postRotate(rotation)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun setupLocation() {
        locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0F, locationListener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_ID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupLocation()
            } else {

            }
        }
    }

    private fun setWeatherData(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(context)
        val location = geocoder.getFromLocation(latitude, longitude, 1)
        val city = location[0].locality
        val retrofit = Retrofit.Builder().baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val openWeatherAPI = retrofit.create(OpenWeatherAPIService::class.java)
        val call = openWeatherAPI.getWeatherData(latitude,
            longitude, "1618c69560b381d9db6a48e2f7b1bd6a")
        call.enqueue(object : Callback<OpenWeatherAPIData> {
            override fun onResponse(call: Call<OpenWeatherAPIData>, response: Response<OpenWeatherAPIData>) {
                if (!response.isSuccessful) {
                    return
                }
                response.body()?.let {
                    val k = it.main.temp
                    val f = Math.round(9/5 * (k - 273.15) + 32)
                    Log.d(TAG, "Temp: $f F")
                    view!!.findViewById<TextView>(R.id.labelWeather).text = "$city: $f F"
                }

            }

            override fun onFailure(call: Call<OpenWeatherAPIData>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }


    private val locationListener: LocationListener = object: LocationListener {

        override fun onLocationChanged(location: Location) {
            Log.d(TAG, "(${location.longitude}:${location.latitude})")
            setWeatherData(location.latitude, location.longitude)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

}