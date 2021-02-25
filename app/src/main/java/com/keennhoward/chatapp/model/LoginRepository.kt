package com.keennhoward.chatapp.model

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginRepository(val application: Application) {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private var firebaseUser: MutableLiveData<FirebaseUser> = MutableLiveData()

    fun signIn(email:String, password:String){

        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {task ->
                if(task.isSuccessful){
                    firebaseUser.postValue(firebaseAuth.currentUser)
                }else{
                    Toast.makeText(application, "Login Failed: ${task.exception!!.message}",
                    Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Log.d("Login", "failed: ${it.message.toString()}")
            }
    }

    fun getFirebaseUser() : MutableLiveData<FirebaseUser>{
        return firebaseUser
    }
}