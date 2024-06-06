package com.sus.calendar.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.sus.calendar.MainActivity
import com.sus.calendar.RetrofitClient
import com.sus.calendar.adapters.JoinedGroupsRecyclerViewAdapter
import com.sus.calendar.databinding.EnterPasswordForGroupBinding

import com.sus.calendar.databinding.MyGroupsBinding
import com.sus.calendar.dtos.GroupforUserDto

class JoinedGroupsFragment:Fragment() {
    private lateinit var joinedBinding:MyGroupsBinding
    private lateinit var bindingdiag:EnterPasswordForGroupBinding
    private val data:MutableList<GroupforUserDto> = mutableListOf()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        joinedBinding = MyGroupsBinding.inflate(inflater, container, false)
        bindingdiag=EnterPasswordForGroupBinding.inflate(inflater,container,false)
        val root = joinedBinding.root
        loaddata()
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
                    joinedBinding.recyclerJoinedGroups.layoutManager=LinearLayoutManager(requireContext())
                    val adapter=JoinedGroupsRecyclerViewAdapter()
                    adapter.data=data
                    joinedBinding.recyclerJoinedGroups.adapter=adapter
                    joinedBinding.backToMain.setOnClickListener{
                        findNavController().navigateUp()
                    }
                    joinedBinding.joinGroup.setOnClickListener{
                        showInputDialog()
                    }
                }
            }
            override fun onFailure(call: Call<List<GroupforUserDto>>, t: Throwable) {
                Log.d("da", "onFailure: a")
            }
        })
    }
    private fun showInputDialog() {
        val dialogView = bindingdiag.root
        val apiService = RetrofitClient.instance
        val parent = dialogView.parent as? ViewGroup
        parent?.removeView(dialogView)
        AlertDialog.Builder(requireContext())
            .setTitle("Введите код")
            .setView(dialogView)
            .setPositiveButton("Создать"){ dialog,_->
                val userId=MainActivity.DataManager.getUserData()!!.id
                val addUser=apiService.addUser(userId,
                    bindingdiag.passworForGroup.text.toString())
                addUser.enqueue(object: Callback<GroupforUserDto>{
                    override fun onResponse(
                        call: Call<GroupforUserDto>,
                        response: Response<GroupforUserDto>
                    ) {
                       if(response.isSuccessful){
                           val code = response.body()
                           val adapter=joinedBinding.recyclerJoinedGroups.adapter as JoinedGroupsRecyclerViewAdapter
                           adapter.data.add(code!!)
                           joinedBinding.recyclerJoinedGroups.adapter=adapter
                           dialog.dismiss()
                       }else{
                           Toast.makeText(
                               requireContext(),
                               "Error: ${response.message()}",
                               Toast.LENGTH_SHORT
                           ).show()
                       }
                    }

                    override fun onFailure(call: Call<GroupforUserDto>, t: Throwable) {
                        Log.d("ser", "onFailure: "+t.message)
                    }

                })
            }
            .setNegativeButton("Отмена") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }
}