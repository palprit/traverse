package com.example.traverse

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.material.button.MaterialButton

class WeatherActivity : AppCompatActivity() {
    private val apiKey = "9ccbac3a50b08c55889d37390021b661"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_weather)

        val city = "Kolkata"
        val tempMain: TextView = findViewById(R.id.tempText)
        val desMain: TextView = findViewById(R.id.descriptText)
        val locationText: TextView = findViewById(R.id.locationText)
        val weatherIcon: ImageView = findViewById(R.id.weatherIcon)
        locationText.text = city

        RetrofitInstance.api.getWeather(city, apiKey).enqueue(object : Callback<ApiStructure> {
            override fun onResponse(
                call: Call<ApiStructure>,
                response: Response<ApiStructure>
            ) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    weatherResponse?.let {
                        val temp = it.main.temp
                        val weather = it.weather[0].main
                        val description = it.weather[0].description

                        desMain.text = "$description"
                        tempMain.text = "$temp"

                        // Update logo based on weather condition
                        updateLogo(weather.toString(), weatherIcon)
                    }
                } else {
                    Toast.makeText(this@WeatherActivity, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ApiStructure>, t: Throwable) {
                Toast.makeText(this@WeatherActivity, "Failed: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })

        val backButton: MaterialButton = findViewById(R.id.backButton)
        val nextButton: MaterialButton = findViewById(R.id.nextButton)

        backButton.setOnClickListener {
            Toast.makeText(this, "Back button clicked", Toast.LENGTH_SHORT).show()
        }

        nextButton.setOnClickListener {
            Toast.makeText(this, "Next button clicked", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateLogo(weather: String, imageView: ImageView) {
        when (weather) {
            "rain" -> imageView.setImageResource(R.drawable.thunderstorm)
            "sunny" -> imageView.setImageResource(R.drawable.sunnyicon)
            "cloudy" -> imageView.setImageResource(R.drawable.cloudy)
            "snow" -> imageView.setImageResource(R.drawable.frost)
            else -> imageView.setImageResource(R.drawable.cloudy)
        }
    }
}
