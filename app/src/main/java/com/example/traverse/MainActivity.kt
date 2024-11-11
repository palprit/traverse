package com.example.traverse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

class MainActivity : AppCompatActivity() {
    private val apiKey = "9ccbac3a50b08c55889d37390021b661"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Handling edge-to-edge layout with system insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // References to the weather views
        val tempView: TextView = findViewById(R.id.home_temp)
        val weatherView: TextView = findViewById(R.id.home_weather)
        val weatherIcon: ImageView = findViewById(R.id.imageView2)

        val city = "Kolkata"

        // Fetching weather data from the API
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

                        tempView.text = "$temp"
                        weatherView.text = "$weather"

                        updateLogo(weather, weatherIcon)
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ApiStructure>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })

        // Handling click on Profile Button to open ProfileActivity
        val profileButton: Button = findViewById(R.id.activity_button)
        profileButton.setOnClickListener {
            val intent = Intent(this@MainActivity, UserActivity::class.java)
            startActivity(intent)
        }
        val weatherButton: ImageView = findViewById(R.id.imageView4)
        weatherButton.setOnClickListener {
            val intent = Intent(this@MainActivity, WeatherActivity::class.java)
            startActivity(intent)
        }
    }

    // Function to update weather icon based on weather condition
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