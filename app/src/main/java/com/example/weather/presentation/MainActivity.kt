package com.example.weather.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.R
import com.example.weather.WeatherApplication
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.presentation.dialogs.CitySelectionDialog
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigation: BottomNavigationView
    lateinit var viewModel: ViewModelMainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelMainActivity(application as WeatherApplication)

        setSupportActionBar(binding.toolbar)

        bottomNavigation = binding.bottomNavigation

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TodayFragment())
                .commit()
        }

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.today -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, TodayFragment())
                        .commit()
                    true
                }
                R.id.week -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, WeekFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_select_city -> {
                CitySelectionDialog(this) { selectedCity ->
                    Toast.makeText(this, "Selected city: $selectedCity", Toast.LENGTH_SHORT).show()
                    supportActionBar?.title = selectedCity
                    viewModel.setSelectedCity(selectedCity)
                }.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}