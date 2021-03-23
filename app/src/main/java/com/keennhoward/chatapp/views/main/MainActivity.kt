package com.keennhoward.chatapp.views.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.keennhoward.chatapp.R
import com.keennhoward.chatapp.data.User
import com.keennhoward.chatapp.databinding.ActivityMainBinding
import com.keennhoward.chatapp.viewmodel.MainViewModel
import com.keennhoward.chatapp.viewmodel.MainViewModelFactory
import com.keennhoward.chatapp.views.LoginActivity
import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.NoInternetDialogPendulum
import org.imaginativeworld.oopsnointernet.snackbars.fire.NoInternetSnackbarFire

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var binding: ActivityMainBinding

    private lateinit var mainViewModel: MainViewModel


    companion object{
        var currentUser: User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        drawerLayout = binding.root

        val factory = MainViewModelFactory(application)

        mainViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        mainViewModel.getCurrentUserData().observe(this, Observer {
            currentUser = it
            if(it != null){
                updateDrawerHeader(it)
            }
        })

        mainViewModel.getFirebaseUser().observe(this, Observer {
            if(it==null){
                val loginIntent = Intent(this, LoginActivity::class.java)
                loginIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(loginIntent)
                finish()
                overridePendingTransition(0,0)
            }else{
                mainViewModel.getToken()
            }
        })

        if(currentUser!=null){
            mainViewModel.setStatusOnline()
        }

        setContentView(drawerLayout)
        navController = findNavController(R.id.fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        binding.navigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val signOut = binding.navigationView.menu.findItem(R.id.sign_out)
        signOut.setOnMenuItemClickListener {
            Toast.makeText(this@MainActivity, "hello", Toast.LENGTH_SHORT).show()
            mainViewModel.signOut()
            true
        }

        // No Internet Dialog: Pendulum
        NoInternetDialogPendulum.Builder(
            this,
            lifecycle
        ).apply {
            dialogProperties.apply {
                connectionCallback = object : ConnectionCallback { // Optional
                    override fun hasActiveConnection(hasActiveConnection: Boolean) {
                        // ...
                    }
                }

                cancelable = false // Optional
                noInternetConnectionTitle = "No Internet" // Optional
                noInternetConnectionMessage =
                    "Check your Internet connection and try again." // Optional
                showInternetOnButtons = true // Optional
                pleaseTurnOnText = "Please turn on" // Optional
                wifiOnButtonText = "Wifi" // Optional
                mobileDataOnButtonText = "Mobile data" // Optional

                onAirplaneModeTitle = "No Internet" // Optional
                onAirplaneModeMessage = "You have turned on the airplane mode." // Optional
                pleaseTurnOffText = "Please turn off" // Optional
                airplaneModeOffButtonText = "Airplane mode" // Optional
                showAirplaneModeOffButtons = true // Optional
            }
        }.build()

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return  navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    }

    private fun updateDrawerHeader(user: User){
        if(binding.navigationView.headerCount >0){
            val headerLayout = binding.navigationView.getHeaderView(0)
            val image = headerLayout.findViewById<ImageView>(R.id.header_image_view)
            val email = headerLayout.findViewById<TextView>(R.id.header_email)
            val username = headerLayout.findViewById<TextView>(R.id.header_username)

            Glide.with(this)
                .load(user.profileImageUrl)
                .into(image)

            username.text = user.username
            email.text = user.email
        }
    }

    override fun onPause() {
        super.onPause()
        if(currentUser!=null){
            mainViewModel.setStatusAway()
        }
    }

    override fun onStop() {
        //mainViewModel.setStatusOffline()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        if(currentUser!=null){
            mainViewModel.setStatusOnline()
        }
    }

    override fun onDestroy() {
        if(currentUser!=null){
            mainViewModel.setStatusOffline()
        }
        super.onDestroy()
    }
}