package com.keennhoward.chatapp.model

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordRepository(val application: Application) {

    private var resetEmailSent = MutableLiveData<Boolean>(false)

    fun getResetEmailResult():MutableLiveData<Boolean>{
        return resetEmailSent
    }

    fun sendResetPasswordEmail(email:String){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Log.d("RESET_PASS", "EMAIL SENT")
                    resetEmailSent.postValue(true)
                }else{
                    resetEmailSent.postValue(false)
                    Toast.makeText(application, "User does not exist", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(application, "Can't Connect to Server", Toast.LENGTH_LONG).show()
                resetEmailSent.postValue(false)
            }
    }
}