package com.keennhoward.chatapp.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.keennhoward.chatapp.R
import com.keennhoward.chatapp.databinding.ActivityResetPasswordBinding
import com.keennhoward.chatapp.viewmodel.ResetPasswordViewModel
import com.keennhoward.chatapp.viewmodel.ResetPasswordViewModelFactory

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding:ActivityResetPasswordBinding
    private lateinit var resetPasswordViewModel: ResetPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        val view = binding.root

        val factory = ResetPasswordViewModelFactory(application)

        resetPasswordViewModel = ViewModelProvider(this,factory).get(ResetPasswordViewModel::class.java)

        resetPasswordViewModel.getResetEmailResult().observe(this, Observer {
            if(it){
                binding.resetFieldScrollView.visibility = View.GONE
                binding.resetFormCompleteMessageTv.visibility = View.VISIBLE
                binding.resetBackButton.visibility = View.VISIBLE
            }else{
                binding.resetFieldScrollView.visibility = View.VISIBLE
                binding.resetFormCompleteMessageTv.visibility = View.GONE
                binding.resetBackButton.visibility = View.GONE
            }
        })

        binding.resetSubmitButton.setOnClickListener {
            if(inputFieldChecker()){
                resetPasswordViewModel.sendResetPasswordEmail(binding.resetEmailEditText.text.toString())
                //Toast.makeText(this, binding.resetEmailEditText.text.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        binding.resetBackButton.setOnClickListener {
            finish()
        }

        setContentView(view)
    }


    private fun inputFieldChecker():Boolean{
        var result = true
        var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        if (!binding.resetEmailEditText.text.toString().trim { it <= ' ' }
                .matches(emailPattern.toRegex())) {
            binding.resetEmailEditText.error = "Please Enter a valid Email"
            result = false
        }

        if (binding.resetEmailEditText.text.toString().trim().isEmpty()) {
            binding.resetEmailEditText.error = "Email Cannot be blank"
            result = false
        }
        return result
    }
}