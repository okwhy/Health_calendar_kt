package com.sus.calendar.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sus.calendar.adapters.GroupMembersNoPhotosRecyclerViewAdapter

import com.sus.calendar.databinding.GroupCardForCreatorBinding


class UsersOfGroup:Fragment() {
    private lateinit var binding: GroupCardForCreatorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=GroupCardForCreatorBinding.inflate(inflater,container,false)
        val root=binding.root
        val args:UsersOfGroupArgs by navArgs()
        binding.textMygroup.text=args.grName
        binding.passwordOfGroup.setText(args.grCode)
        binding.recyclerMygroup.layoutManager=LinearLayoutManager(requireContext())

        val adapter = GroupMembersNoPhotosRecyclerViewAdapter(args.id)

        adapter.data= args.users.toMutableList()
        binding.recyclerMygroup.adapter=adapter
        binding.bacKToGroups.setOnClickListener{
            findNavController().navigateUp()
        }
        return root
    }
}