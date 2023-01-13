package com.example.weatherforecast

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import com.example.weatherforecast.databinding.ActivityMainBinding
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val client = OkHttpClient()
    private val apiKey = "40c1309c255992b7b8cc59f2f8f63679"
    private val cityUrl = "https://api.openweathermap.org/geo/1.0/direct?q=%s,IR&limit=5&appid=%s"
    private val forecastUrl = "https://api.openweathermap.org/data/2.5/forecast?cnt=5&units=metric&lat=%s&lon=%s&appid=%s"
    private val iconUrl = "https://openweathermap.org/img/wn/%s@2x.png"

    private val logTag = "WTHR"

    private var currentLocation: Location? = null
    lateinit var locationManager: LocationManager


    private val states = listOf("Tehran", "Mazandaran", "Khozestan")
    private val cities = mapOf("Tehran" to listOf("Tehran", "Varamin"), "Mazandaran" to listOf("Babol", "Sari"), "Khozestan" to listOf("Ahvaz", "Nahavand"))

    private lateinit var currentState: String
    private lateinit var currentCity: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.errorLayout.visibility = View.GONE
        binding.weatherLayout.visibility = View.GONE

        var stateSpinner = binding.spinnerState
        val stateAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, states)
        stateSpinner.adapter = stateAdapter

        stateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                currentState = states[stateSpinner.selectedItemId.toInt()]

                currentCity = null.toString()
                val citySpinner = binding.spinnerCity
                val cityAdapter: ArrayAdapter<String> =
                    ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, cities[currentState]!!)
                citySpinner.adapter = cityAdapter
                citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        currentCity = cities[currentState]!![citySpinner.selectedItemId.toInt()]
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        binding.submitButton.setOnClickListener{searchByName(currentCity)}
        binding.locationButton.setOnClickListener{getLocation()}
    }

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
            false
        } else {
            true
        }
    }

    private fun getLocation() {
        if (!isLocationPermissionGranted()) {
            return
        }
        var locationByGps : Location? = null
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val gpsLocationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                locationByGps = location
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        if (hasGps) {
            try {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    0F,
                    gpsLocationListener
                )
                val lastKnownLocationByGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                lastKnownLocationByGps?.let {
                    locationByGps = lastKnownLocationByGps
                }
                currentLocation = locationByGps
                var latitude = currentLocation?.latitude
                var longitude = currentLocation?.longitude
                if (longitude != null && latitude != null) {
                    search(latitude, longitude)
                }
            } catch (e: SecurityException){
                return
            }
        }
    }

    private fun searchByName(cityName: String) {
        val request = Request.Builder()
            .url(String.format(cityUrl, cityName, apiKey))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                this@MainActivity.runOnUiThread {
                    binding.weatherLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.VISIBLE
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val result = response.body()?.string()?.let { JSONArray(it) }
                val lat = (result!!.get(0) as JSONObject).get("lat").toString().toDouble()
                val lon = (result.get(0) as JSONObject).get("lon").toString().toDouble()
                search(lat, lon)
            }
        })
    }

    private fun search(latitude: Double, longitude: Double) {
        val request = Request.Builder()
            .url(String.format(forecastUrl, latitude.toString(), longitude.toString(), apiKey))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                this@MainActivity.runOnUiThread {
                    binding.weatherLayout.visibility = View.GONE
                    binding.errorLayout.visibility = View.VISIBLE
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val result = response.body()?.string()?.let { JSONObject(it) }
                val days = result!!.get("list") as JSONArray
                this@MainActivity.runOnUiThread {
                    binding.errorLayout.visibility = View.GONE
                    binding.weatherLayout.visibility = View.VISIBLE
                    binding.cityText.text = (result.get("city") as JSONObject).get("name").toString()
                    loadImage((((days.get(0) as JSONObject).get("weather") as JSONArray).get(0) as JSONObject).get("icon").toString())
                    binding.dateTime.text = (days.get(0) as JSONObject).get("dt_txt").toString()
                    binding.todayTempText.text = ((days.get(0) as JSONObject).get("main") as JSONObject).get("temp").toString()
                    binding.tomorrowTempText.text = ((days.get(1) as JSONObject).get("main") as JSONObject).get("temp").toString()
                    binding.twoDaysTempText.text = ((days.get(2) as JSONObject).get("main") as JSONObject).get("temp").toString()
                    binding.threeDaysTempText.text = ((days.get(3) as JSONObject).get("main") as JSONObject).get("temp").toString()
                    binding.fourDaysTempText.text = ((days.get(4) as JSONObject).get("main") as JSONObject).get("temp").toString()
                }
            }
        })
    }

    private fun loadImage(icon: String) {
        val imageView = binding.todayIcon

        // Declaring executor to parse the URL
        val executor = Executors.newSingleThreadExecutor()

        // Once the executor parses the URL
        // and receives the image, handler will load it
        // in the ImageView
        val handler = Handler(Looper.getMainLooper())

        // Initializing the image
        var image: Bitmap? = null

        // Only for Background process (can take time depending on the Internet speed)
        executor.execute {

            // Image URL
            val imageURL = String.format(iconUrl, icon)

            // Tries to get the image and post it in the ImageView
            // with the help of Handler
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)

                // Only for making changes in UI
                handler.post {
                    imageView.setImageBitmap(image)
                }
            }

            // If the URL does not point to
            // image or any other kind of failure
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}