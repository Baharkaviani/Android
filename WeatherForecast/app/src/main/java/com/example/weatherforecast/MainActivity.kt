package com.example.weatherforecast

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.Executors
import com.example.weatherforecast.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val states = listOf("Tehran", "Yazd", "Isfahan", "Fars", "Kermanshah")
    private val cities = mapOf("Tehran" to listOf("Tehran", "Eslamshahr", "Shahriar", "Malard", "Varamin"),
        "Yazd" to listOf("Yazd", "Meybod", "Nurabad", "Bafq", "Hamidiya"),
        "Isfahan" to listOf("Isfahan", "Kashan", "Najafabad", "Shahreza", "Baharestan", "Falavarjan"),
        "Fars" to listOf("Shiraz", "Marvdasht", "Jahrom", "Fasa", "Kazerun", "Darab"),
        "Kermanshah" to listOf("Kermanshah", "Kangavar", "Harsin", "Sonqor", "Paveh"))

    private val client = OkHttpClient()
    private var selectedLocation: Location? = null
    lateinit var locationManager: LocationManager
    private lateinit var selectedState: String
    private lateinit var selectedCity: String

    private fun requestCityName(cityName: String) {
        val request = Request.Builder()
            .url(String.format("https://api.openweathermap.org/geo/1.0/direct?q=%s,IR&limit=5&appid=%s",
                               cityName,
                               "40c1309c255992b7b8cc59f2f8f63679"))
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
                requestToFindWeatherInfo(lat, lon)
            }
        })
    }

    private fun requestToFindWeatherInfo(latitude: Double, longitude: Double) {
        val request = Request.Builder()
            .url(String.format("https://api.openweathermap.org/data/2.5/forecast?cnt=5&units=metric&lat=%s&lon=%s&appid=%s",
                               latitude.toString(),
                               longitude.toString(),
                               "40c1309c255992b7b8cc59f2f8f63679"))
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
                    binding.dateTime.text = (days.get(0) as JSONObject).get("dt_txt").toString()

                    // show the weather for 5 days
                    val temp = ((days.get(0) as JSONObject).get("main") as JSONObject).get("temp").toString()
                    binding.todayTempText.text = temp
                    binding.secondDayTempText.text = ((days.get(1) as JSONObject).get("main") as JSONObject).get("temp").toString()
                    binding.thirdDayTempText.text = ((days.get(2) as JSONObject).get("main") as JSONObject).get("temp").toString()
                    binding.fourthDayTempText.text = ((days.get(3) as JSONObject).get("main") as JSONObject).get("temp").toString()
                    binding.fifthDayTempText.text = ((days.get(4) as JSONObject).get("main") as JSONObject).get("temp").toString()

                    val weather_image = binding.todayIcon
                    weather_image.setImageResource(R.drawable.sunny)

                    if (temp.toDouble() > 2)
                        weather_image.setImageResource(R.drawable.sunny)
                    else if (temp.toDouble() <= 2 && temp.toDouble() > 0)
                        weather_image.setImageResource(R.drawable.partly_cloudy)
                    else if (temp.toDouble() <= 0 && temp.toDouble() > -2)
                        weather_image.setImageResource(R.drawable.rainy)
                    else if (temp.toDouble() <= -2 && temp.toDouble() > -4)
                        weather_image.setImageResource(R.drawable.rainy2)
                    else
                        weather_image.setImageResource(R.drawable.rainy3)
                }
            }
        })
    }

    private fun checkLocationPermission(): Boolean {
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
                selectedState = states[stateSpinner.selectedItemId.toInt()]

                selectedCity = null.toString()
                val citySpinner = binding.spinnerCity
                val cityAdapter: ArrayAdapter<String> =
                    ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, cities[selectedState]!!)
                citySpinner.adapter = cityAdapter
                citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        selectedCity = cities[selectedState]!![citySpinner.selectedItemId.toInt()]
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
        binding.submitButton.setOnClickListener{requestCityName(selectedCity)}
        binding.locationButton.setOnClickListener{getLocation()}
    }

    private fun getLocation() {
        // first check if the location permission is allowed or not
        if (!checkLocationPermission()) {
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
                selectedLocation = locationByGps
                var latitude = selectedLocation?.latitude
                var longitude = selectedLocation?.longitude
                if (longitude != null && latitude != null) {
                    requestToFindWeatherInfo(latitude, longitude)
                }
            } catch (e: SecurityException){
                return
            }
        }
    }
}