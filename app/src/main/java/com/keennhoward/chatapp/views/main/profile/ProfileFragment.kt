package com.keennhoward.chatapp.views.main.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.keennhoward.chatapp.R
import com.keennhoward.chatapp.databinding.FragmentProfileBinding
import com.keennhoward.chatapp.viewmodel.ProfileViewModel
import com.keennhoward.chatapp.viewmodel.ProfileViewModelFactory
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
            profileViewModel.saveUserChanges(MainActivity.currentUser!!.profileImageUrl, binding.profileUsernameEdit.text.toString())

        }

        return view
    }
}