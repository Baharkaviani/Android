package com.example.hafezdivination

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.hafezdivination.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var _appBarConfiguration: AppBarConfiguration
    private lateinit var _activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_activityMainBinding.root)

        setSupportActionBar(_activityMainBinding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        _appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, _appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(_appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}