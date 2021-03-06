package com.keennhoward.chatapp.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.keennhoward.chatapp.databinding.ActivityLoginBinding
import com.keennhoward.chatapp.viewmodel.LoginViewModel
import com.keennhoward.chatapp.viewmodel.LoginViewModelFactory
import com.keennhoward.chatapp.views.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel:LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root

        //supportActionBar!!.hide()
        //window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //.LayoutParams.FLAG_FULLSCREEN)

        //hideSystemUI()

        val factory = LoginViewModelFactory(application)

        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        binding.loginButton.setOnClickListener {
            if(loginFieldChecker()){
                loginViewModel.signIn(binding.loginEmail.text.toString(),binding.loginPassword.text.toString())
            }else{

            }
        }

        loginViewModel.getFirebaseUser().observe(this, Observer {
            if(it!=null){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        binding.loginRegister.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }
        binding.loginReset.setOnClickListener {
            val resetPasswordIntent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(resetPasswordIntent)
        }



        setContentView(view)
    }

    private fun loginFieldChecker():Boolean{
        var result = true
        var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        if (binding.loginPassword.text.toString().trim().isEmpty()) {
            binding.loginPassword.error = "Password Cannot be blank"
            result = false
        }

        if (binding.loginEmail.text.toString().trim().isEmpty()) {
            binding.loginEmail.error = "Email Cannot be blank"
            result = false
        }

        //format check
        if (!binding.loginEmail.text.toString().trim { it <= ' ' }
                .matches(emailPattern.toRegex())) {
            binding.loginEmail.error = "Please Enter a valid Email"
            result = false
        }

        if (binding.loginPassword.text.toString().contains(" ")) {
            binding.loginPassword.error = "No Spaces Allowed"
            result = false
        }
        if(binding.loginPassword.text.toString().trim().length < 6){
            binding.loginPassword.error = "Password Must be at least 6 characters"
            result = false
        }
        return result
    }

    private fun hideSystemUI() {

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        // Hide the nav bar and status bar
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // Hide nav bar
                        or View.SYSTEM_UI_FLAG_FULLSCREEN // Hide status bar
                )
    }
}