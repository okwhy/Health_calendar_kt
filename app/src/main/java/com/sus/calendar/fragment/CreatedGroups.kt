package com.sus.calendar.fragment

import android.R
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.sus.calendar.MainActivity
import com.sus.calendar.RetrofitClient
import com.sus.calendar.adapters.CreatorsGroupRecyclerViewAdapter

import com.sus.calendar.databinding.CarGroupForCreaterBinding
import com.sus.calendar.databinding.CardGroupBinding
import com.sus.calendar.databinding.CreateGroupBinding
import com.sus.calendar.databinding.EnterForGroupBinding
import com.sus.calendar.dtos.getgroupcreator.GroupCreatorForCreatorDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CreatedGroups:Fragment() {
    private lateinit var createGroupBinding: CreateGroupBinding
    private lateinit var enter_for_group: EnterForGroupBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        createGroupBinding = CreateGroupBinding.inflate(inflater,container,false)
        enter_for_group = EnterForGroupBinding.inflate(inflater,container,false)
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


        createGroupBinding.createGroup.setOnClickListener{
            showInputDialog()
        }

        return root
    }

    private fun showInputDialog() {
        val dialogView = enter_for_group.enterForGroup
        val apiService= RetrofitClient.instance
        val parent = dialogView.parent as? ViewGroup
        parent?.removeView(dialogView)
        AlertDialog.Builder(requireContext())
            .setTitle("Введите название группы")
            .setView(dialogView)
            .setPositiveButton("Создать") { dialog, _ ->
                val user_id = MainActivity.DataManager.getUserData()!!.id
                val create_group = apiService.create_group(user_id,
                    enter_for_group.nameForGroup.text.toString()
                )

                create_group.enqueue(object : Callback<GroupCreatorForCreatorDto> {

                    override fun onResponse(
                        call: Call<GroupCreatorForCreatorDto>,
                        response: Response<GroupCreatorForCreatorDto>
                    ) {
                        if (response.isSuccessful) {
                            val code = response.body()
                            val adapter=createGroupBinding.recyclerAllGroup.adapter as CreatorsGroupRecyclerViewAdapter
                            adapter.data.add(code!!)
                            createGroupBinding.recyclerAllGroup.adapter=adapter
                            dialog.dismiss()

                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Error: ${response.message()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    override fun onFailure(call: Call<GroupCreatorForCreatorDto>, t: Throwable) {
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