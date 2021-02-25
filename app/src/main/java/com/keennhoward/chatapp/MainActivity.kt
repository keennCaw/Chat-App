package com.keennhoward.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.keennhoward.chatapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var listener:NavController.OnDestinationChangedListener

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        drawerLayout = binding.root



        setContentView(drawerLayout)
        navController = findNavController(R.id.fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        binding.navigationView.setupWithNavController(navController)

        setupActionBarWithNavController(navController, appBarConfiguration)


        val signOut = binding.navigationView.menu.findItem(R.id.sign_out)
        signOut.setOnMenuItemClickListener {

            Toast.makeText(this@MainActivity, "hello", Toast.LENGTH_SHORT).show()

            true
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return  navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    }
}