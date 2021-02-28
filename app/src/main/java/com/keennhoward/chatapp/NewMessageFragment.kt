package com.keennhoward.chatapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.keennhoward.chatapp.databinding.FragmentNewMessageBinding
import com.keennhoward.chatapp.viewmodel.NewMessageViewModel
import com.keennhoward.chatapp.viewmodel.NewMessageViewModelFactory

class NewMessageFragment : Fragment(), UserClickListener {

    private lateinit var newMessageViewModel: NewMessageViewModel
    private lateinit var newMessageAdapter: NewMessageAdapter

    private var _binding: FragmentNewMessageBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNewMessageBinding.inflate(inflater, container, false)

        val view = binding.root


        val factory = NewMessageViewModelFactory()
        newMessageViewModel = ViewModelProvider(this, factory).get(NewMessageViewModel::class.java)

        newMessageAdapter = NewMessageAdapter(requireContext(), this)

        binding.userListRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newMessageAdapter
        }

        newMessageViewModel.getUserList().observe(requireActivity(), Observer {
            newMessageAdapter.submitList(it)
        })

        newMessageViewModel.fetchUsers()


        return view
    }

    override fun onUserItemClickListener(user: User) {
        Toast.makeText(requireContext(), "${user.username}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}