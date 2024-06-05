package com.sus.calendar.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.sus.calendar.MainActivity
import com.sus.calendar.RetrofitClient
import com.sus.calendar.adapters.JoinedGroupsRecyclerViewAdapter

import com.sus.calendar.databinding.MyGroupsBinding
import com.sus.calendar.dtos.GroupforUserDto

class JoinedGroupsFragment:Fragment() {
    private lateinit var joinedBinding:MyGroupsBinding
    private val data:MutableList<GroupforUserDto> = mutableListOf()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        joinedBinding = MyGroupsBinding.inflate(inflater, container, false)
        val root = joinedBinding.root
        loaddata()
        joinedBinding.recyclerJoinedGroups.layoutManager=LinearLayoutManager(requireContext())
        val adapter=JoinedGroupsRecyclerViewAdapter()
        adapter.data=data
        joinedBinding.recyclerJoinedGroups.adapter=adapter
        joinedBinding.backToMain.setOnClickListener{
            findNavController().navigateUp()
        }
        return root
    }
    private fun loaddata(){
        val apiService=RetrofitClient.instance
        var user_id = MainActivity.DataManager.getUserData()!!.id
        val call_member_groups = apiService.get_member_groups(user_id)
        call_member_groups.enqueue(object : Callback<List<GroupforUserDto>> {
            override fun onResponse(
                call: Call<List<GroupforUserDto>>,
                response: Response<List<GroupforUserDto>>
            ) {
                if (response.isSuccessful) {
                    data.addAll(response.body()!!)
                }
            }
            override fun onFailure(call: Call<List<GroupforUserDto>>, t: Throwable) {
                Log.d("da", "onFailure: a")
            }
        })
    }
}