package com.keennhoward.chatapp.views.main.messages

import android.content.Intent
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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.keennhoward.chatapp.data.LatestMessage
import com.keennhoward.chatapp.views.main.newmessage.NewMessageFragment
import com.keennhoward.chatapp.R
import com.keennhoward.chatapp.data.User
import com.keennhoward.chatapp.databinding.FragmentMessagesBinding
import com.keennhoward.chatapp.viewmodel.MessagesViewModel
import com.keennhoward.chatapp.viewmodel.MessagesViewModelFactory
import com.keennhoward.chatapp.views.chatlog.ChatLogActivity
import com.keennhoward.chatapp.views.globalchat.GlobalChatActivity
import com.keennhoward.chatapp.views.main.MainActivity

class MessagesFragment : Fragment(),
    MessageItemClickListener {

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

        val messagesAdapter =
            MessagesAdapter(
                this,
                requireContext()
            )



        binding.latestMessagesRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = messagesAdapter
            addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))
        }

        messagesViewModel = ViewModelProvider(requireActivity(),factory).get(MessagesViewModel::class.java)

        messagesViewModel.getLatestMessages().observe(requireActivity(), Observer {
            messagesAdapter.submitList(ArrayList<LatestMessage>(it))



            //Log.d("LatestMessageFragment", it.toString())
        })

        binding.createMessage.setOnClickListener {
            onCreateMessageButtonClicked()
        }

        binding.messageSearchUsers.setOnClickListener {
            Navigation.findNavController(view).navigate(MessagesFragmentDirections.actionMessagesFragmentToNewMessageFragment())
            onCreateMessageButtonClicked()
        }

        binding.messageRandomUser.setOnClickListener {
            val user = messagesViewModel.getRandomUser()
            if(user!=null){
                //Toast.makeText(requireActivity(), user.toString(), Toast.LENGTH_SHORT).show()
                val intent = Intent(requireActivity(), ChatLogActivity::class.java)
                intent.putExtra(NewMessageFragment.USER_KEY, user)
                startActivity(intent)
            }
        }

        binding.messageGlobal.setOnClickListener {
            val intent = Intent(requireActivity(), GlobalChatActivity::class.java)
            startActivity(intent)
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
            binding.messageRandomUser.visibility = View.VISIBLE
            binding.messageSearchUsers.visibility = View.VISIBLE
            binding.messageGlobal.visibility = View.VISIBLE
        }else{
            binding.messageRandomUser.visibility = View.INVISIBLE
            binding.messageSearchUsers.visibility = View.INVISIBLE
            binding.messageGlobal.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if(!clicked){
            binding.messageSearchUsers.startAnimation(fromBottom)
            binding.messageRandomUser.startAnimation(fromBottom)
            binding.messageGlobal.startAnimation(fromBottom)
            binding.createMessage.startAnimation(rotateOpen)
        }else{
            binding.messageSearchUsers.startAnimation(toBottom)
            binding.messageRandomUser.startAnimation(toBottom)
            binding.messageGlobal.startAnimation(toBottom)
            binding.createMessage.startAnimation(rotateClose)
        }
    }
    private fun setClickable(clicked: Boolean){
        if(!clicked){
            binding.messageSearchUsers.isClickable = true
            binding.messageRandomUser.isClickable = true
            binding.messageGlobal.isClickable = true
        }else{
            binding.messageSearchUsers.isClickable = false
            binding.messageRandomUser.isClickable = false
            binding.messageGlobal.isClickable = false
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMessageItemClickListener(messageInfo: LatestMessage) {
        var user:User
        if (MainActivity.currentUser!!.uid == messageInfo.toId){
            user = User(
                messageInfo.fromId,
                messageInfo.username,
                messageInfo.profileImageUrl,
                messageInfo.email,
                messageInfo.token,
                messageInfo.status
            )
        }else{
            user = User(
                messageInfo.toId,
                messageInfo.username,
                messageInfo.profileImageUrl,
                messageInfo.email,
                messageInfo.token,
                messageInfo.status
            )
        }


        val intent = Intent(requireActivity(), ChatLogActivity::class.java)
        intent.putExtra(NewMessageFragment.USER_KEY, user)
        startActivity(intent)
    }


}

