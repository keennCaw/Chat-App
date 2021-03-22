package com.keennhoward.chatapp.views.main.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.keennhoward.chatapp.R
import com.keennhoward.chatapp.databinding.FragmentProfileBinding
import com.keennhoward.chatapp.viewmodel.ProfileViewModel
import com.keennhoward.chatapp.viewmodel.ProfileViewModelFactory
import com.keennhoward.chatapp.views.RegisterActivity
import com.keennhoward.chatapp.views.main.MainActivity

class ProfileFragment : Fragment() {

    private lateinit var _binding:FragmentProfileBinding
    private val binding get() = _binding!!

    private lateinit var profileViewModel:ProfileViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        val view = binding.root

        val factory = ProfileViewModelFactory(requireActivity().application)

        profileViewModel = ViewModelProvider(requireActivity(),factory).get(ProfileViewModel::class.java)

        binding.profileEmailEdit.text = MainActivity.currentUser!!.email
        binding.profileUsernameEdit.setText(MainActivity.currentUser!!.username)

        Glide.with(requireActivity())
            .load(MainActivity.currentUser!!.profileImageUrl)
            .into(binding.profileImage)

        binding.profileUsernameEdit.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.profileEditButtons.visibility = View.VISIBLE
            }

        })

        binding.profileEditCancel.setOnClickListener {
            binding.profileEditButtons.visibility = View.GONE
        }

        binding.profileEditSave.setOnClickListener {
            binding.profileEditButtons.visibility = View.GONE
            if(inputFieldChecker()){
                if(selectedPhotoUri!=null){
                    profileViewModel.updateUserWithImage(selectedPhotoUri!!, binding.profileUsernameEdit.text.toString(),MainActivity.currentUser!!.profileImageId)
                }else{
                    profileViewModel.updateUser(MainActivity.currentUser!!.profileImageUrl, binding.profileUsernameEdit.text.toString())
                }
            }
        }

        binding.profileImageEdit.setOnClickListener {
            val selectImageIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            selectImageIntent.type = "image/*"
            startActivityForResult(
                selectImageIntent,
                RegisterActivity.SELECT_IMAGE_REQUEST
            )
        }

        return view
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RegisterActivity.SELECT_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {

            //binding.registerImageTv.visibility = View.GONE

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedPhotoUri)

            binding.profileImage.setImageBitmap(bitmap)
        }
    }

    private fun inputFieldChecker(): Boolean {
        var result = true
        if (binding.profileUsernameEdit.text.toString().trim().isEmpty()) {
            Toast.makeText(requireActivity(), "Username Cannot be blank", Toast.LENGTH_SHORT).show()
            result = false
        }
        return result
    }
}