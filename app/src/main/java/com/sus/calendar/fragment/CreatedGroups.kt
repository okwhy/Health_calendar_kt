package com.sus.calendar.fragment

import android.R
import android.app.AlertDialog
import android.os.Bundle
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
    private lateinit var card: CarGroupForCreaterBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        createGroupBinding = CreateGroupBinding.inflate(inflater,container,false)
        enter_for_group = EnterForGroupBinding.inflate(inflater,container,false)
        card = CarGroupForCreaterBinding.inflate(inflater,container,false)


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

            showInputDialog();
        }

        return root
    }

    private fun showInputDialog() {
        val inflater = layoutInflater
        val dialogView = enter_for_group.enterForGroup
        val editTextInput = enter_for_group.nameForGroup
        val apiService= RetrofitClient.instance
        val args:CreatedGroupsArgs by navArgs()

        AlertDialog.Builder(requireContext())
            .setTitle("Введите значение")
            .setView(dialogView)
            .setPositiveButton("Создать") { dialog, which ->
                var userInputValue = editTextInput.text.toString()

                val user_id = MainActivity.DataManager.getUserData()?.id?.toLong()
                val create_group = user_id?.let { apiService.create_group(it,userInputValue) }

                if (create_group != null) {
                    create_group.enqueue(object : Callback<String> {
                        override fun onResponse(
                            call: Call<String>,
                            response: Response<String>
                        ) {
                            if (response.isSuccessful) {
                                val code = response.body()

                                val code_temp = 0


                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Error: ${response.message()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}