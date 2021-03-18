package com.keennhoward.chatapp.views.main.newmessage

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.keennhoward.chatapp.R
import com.keennhoward.chatapp.data.User
import com.keennhoward.chatapp.databinding.FragmentNewMessageBinding
import com.keennhoward.chatapp.viewmodel.NewMessageViewModel
import com.keennhoward.chatapp.viewmodel.NewMessageViewModelFactory
import com.keennhoward.chatapp.views.chatlog.ChatLogActivity

class NewMessageFragment : Fragment(),
    UserClickListener {

    private lateinit var newMessageViewModel: NewMessageViewModel
    private lateinit var newMessageAdapter: NewMessageAdapter

    private var _binding: FragmentNewMessageBinding? = null

    private val binding get() = _binding!!

    companion object{
        const val USER_KEY = "USER_KEY"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNewMessageBinding.inflate(inflater, container, false)

        val view = binding.root


        val factory = NewMessageViewModelFactory()
        newMessageViewModel = ViewModelProvider(this, factory).get(NewMessageViewModel::class.java)

        newMessageAdapter =
            NewMessageAdapter(
                requireContext(),
                this
            )

        binding.userListRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newMessageAdapter
        }

        newMessageViewModel.getUserList().observe(requireActivity(), Observer {
            newMessageAdapter.modifyList(it)
        })

        newMessageViewModel.fetchUsers()


        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_message_menu,menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                (binding.userListRecyclerview.adapter as NewMessageAdapter).filter(newText)
                return true
            }

        })


        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onUserItemClickListener(user: User) {
        Toast.makeText(requireContext(), "${user.username}", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireActivity(), ChatLogActivity::class.java)
        intent.putExtra(USER_KEY, user)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}