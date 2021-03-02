package com.keennhoward

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.keennhoward.chatapp.R
import com.keennhoward.chatapp.databinding.FragmentMessagesBinding
import com.keennhoward.chatapp.viewmodel.MessagesViewModel
import com.keennhoward.chatapp.viewmodel.MessagesViewModelFactory

class MessagesFragment : Fragment() {

    private var _binding:FragmentMessagesBinding? = null
    private val binding get() = _binding!!

    private lateinit var messagesViewModel:MessagesViewModel

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireActivity(), R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireActivity(), R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireActivity(), R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireActivity(), R.anim.to_bottom_anim) }

    private var clicked = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        val view = binding.root

        val factory = MessagesViewModelFactory()

        messagesViewModel = ViewModelProvider(requireActivity(),factory).get(MessagesViewModel::class.java)
        //messagesViewModel.listenForLatestMassages()

        messagesViewModel.getLatestMessages().observe(requireActivity(), Observer {
            Log.d("latest Messages", it.toString())
        })


        binding.createMessage.setOnClickListener {
            onCreateMessageButtonClicked()
        }

        binding.messageSearchUsers.setOnClickListener {
            Navigation.findNavController(view).navigate(MessagesFragmentDirections.actionMessagesFragmentToNewMessageFragment())
            onCreateMessageButtonClicked()
        }

        binding.messageLocationUsers.setOnClickListener {
            Toast.makeText(requireActivity(), "search a user by location", Toast.LENGTH_SHORT).show()
        }


        return view
    }

    private fun onCreateMessageButtonClicked(){
        setAnimation(clicked)
        setVisibility(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if(!clicked){
            binding.messageLocationUsers.visibility = View.VISIBLE
            binding.messageSearchUsers.visibility = View.VISIBLE
        }else{
            binding.messageLocationUsers.visibility = View.INVISIBLE
            binding.messageSearchUsers.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if(!clicked){
            binding.messageSearchUsers.startAnimation(fromBottom)
            binding.messageLocationUsers.startAnimation(fromBottom)
            binding.createMessage.startAnimation(rotateOpen)
        }else{
            binding.messageSearchUsers.startAnimation(toBottom)
            binding.messageLocationUsers.startAnimation(toBottom)
            binding.createMessage.startAnimation(rotateClose)
        }
    }
    private fun setClickable(clicked: Boolean){
        if(!clicked){
            binding.messageSearchUsers.isClickable = true
            binding.messageLocationUsers.isClickable = true
        }else{
            binding.messageSearchUsers.isClickable = false
            binding.messageLocationUsers.isClickable = false
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

