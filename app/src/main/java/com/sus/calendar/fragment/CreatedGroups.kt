package com.sus.calendar.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.sus.calendar.adapters.CreatorsGroupRecyclerViewAdapter
import com.sus.calendar.databinding.CreateGroupBinding

class CreatedGroups:Fragment() {
    private lateinit var createGroupBinding: CreateGroupBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        createGroupBinding=CreateGroupBinding.inflate(inflater,container,false)
        val root=createGroupBinding.root
        val args:CreatedGroupsArgs by navArgs()
        val manager=LinearLayoutManager(requireContext())
        val adapter=CreatorsGroupRecyclerViewAdapter()
        adapter.data= args.groups.toMutableList()
        createGroupBinding.recyclerAllGroup.layoutManager=manager
        createGroupBinding.recyclerAllGroup.adapter=adapter
        createGroupBinding.backListMyGroups.setOnClickListener{
            findNavController().navigateUp()
        }
        return root
    }
}