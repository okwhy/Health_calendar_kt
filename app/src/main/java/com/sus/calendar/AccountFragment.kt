package com.sus.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sus.calendar.databinding.EnterBinding
import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.sus.calendar.adapters.CreatorsGroupRecyclerViewAdapter
import com.sus.calendar.adapters.GroupMembersRecyclerViewAdapter
import com.sus.calendar.adapters.JoinedGroupsRecyclerViewAdapter
import com.sus.calendar.databinding.AccountLayoutBinding
import com.sus.calendar.databinding.AccountPageBinding
import com.sus.calendar.databinding.CardGroupBinding
import com.sus.calendar.databinding.CreateGroupBinding
import com.sus.calendar.databinding.GroupCardForCreatorBinding
import com.sus.calendar.databinding.MyGroupsBinding
import com.sus.calendar.databinding.RegistrationBinding
import com.sus.calendar.dtos.GroupforUserDto
import com.sus.calendar.dtos.UserDTO
import com.sus.calendar.dtos.getgroupcreator.GroupCreatorForCreatorDto
import com.sus.calendar.dtos.getgroupcreator.subdtos.GroupForCreatorDTO


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public var auth_flag = 0;
private lateinit var enter_binding: EnterBinding


class AccountFragment : Fragment() {
    private lateinit var account_layout_binding: AccountLayoutBinding
    private lateinit var register_layout_binding: RegistrationBinding
    private lateinit var user_member_groups_binding: MyGroupsBinding
    private lateinit var group_card_for_member:CardGroupBinding
    private lateinit var user_creator_groups_binding:CreateGroupBinding
    private lateinit var joinadapter: JoinedGroupsRecyclerViewAdapter
    private lateinit var creatoradapter: CreatorsGroupRecyclerViewAdapter
    private lateinit var groupCardForCreatorBinding: GroupCardForCreatorBinding
    @OptIn(UnstableApi::class) override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val apiService = RetrofitClient.instance

        enter_binding = EnterBinding.inflate(inflater,container,false)
        joinadapter = JoinedGroupsRecyclerViewAdapter()
        user_creator_groups_binding = CreateGroupBinding.inflate(inflater,container,false)
        groupCardForCreatorBinding=GroupCardForCreatorBinding.inflate(inflater,container,false)
        account_layout_binding = AccountLayoutBinding.inflate(inflater,container,false)
        register_layout_binding = RegistrationBinding.inflate(inflater,container,false)
        user_member_groups_binding = MyGroupsBinding.inflate(inflater,container,false)
        group_card_for_member = CardGroupBinding.inflate(inflater,container,false)
        creatoradapter = CreatorsGroupRecyclerViewAdapter(enter_binding,user_creator_groups_binding,groupCardForCreatorBinding)

        val enter_layout = enter_binding.enterLayout

        val enter_layout_embedded = enter_binding.enterLayoutEmbedded



        val account_layout_binding_layout = AccountPageBinding.inflate(inflater,container,false)

        val account_layout = account_layout_binding_layout.accountLayoutPage


        if(auth_flag == 0) {
            if(MainActivity.DataManager.getUserData() != null)
            {
                enter_layout.removeView(enter_layout_embedded)

                enter_layout.addView(account_layout)
            }
        }
        joinadapter = JoinedGroupsRecyclerViewAdapter()

        val login_edit = enter_binding.editTextEmail

        val password_edit = enter_binding.editTextPassword

        val enterbutton = enter_binding.buttonRegister

        val registerbutton = enter_binding.Regist

        var triedlogin = login_edit.text

        var triedpass = password_edit.text

        //==================================================//



        val account_exit_button = account_layout_binding_layout.exit

        val member_group_button = account_layout_binding_layout.MyGroup



        val user_member_layout = user_member_groups_binding.UserGroupsLayout

        member_group_button.setOnClickListener()
        {
            var user_id = MainActivity.DataManager.getUserData()!!.id

            val call_member_groups = apiService.get_member_groups(user_id)

            call_member_groups.enqueue(object : Callback<List<GroupforUserDto>> {
                override fun onResponse(call: Call<List<GroupforUserDto>>, response: Response<List<GroupforUserDto>>) {
                    if (response.isSuccessful) {
                        val groups = response.body()

                        enter_layout.removeView(account_layout)

                        enter_layout.addView(user_member_layout)
                        joinadapter.data= groups as MutableList<GroupforUserDto>
                        val manager=LinearLayoutManager(requireContext())
                        user_member_groups_binding.recyclerJoinedGroups.layoutManager=manager
                        user_member_groups_binding.recyclerJoinedGroups.adapter=joinadapter


                    } else {
                        Toast.makeText(requireContext(), "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<GroupforUserDto>>, t: Throwable) {
                    // Обработка ошибок сети или других ошибок
                    Log.e("RetrofitError", "Ошибка: ${t.message}", t)
                    Toast.makeText(requireContext(), "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        val creator_group_button = account_layout_binding_layout.MyGroups



        val user_creator_layout = user_creator_groups_binding.GroupCreatorLayout



        creator_group_button.setOnClickListener()
        {
            var user_id = MainActivity.DataManager.getUserData()!!.id

            val call_creator_groups = apiService.get_creator_groups(user_id.toLong())

            call_creator_groups.enqueue(object : Callback<List<GroupCreatorForCreatorDto>> {
                override fun onResponse(call: Call<List<GroupCreatorForCreatorDto>>, response: Response<List<GroupCreatorForCreatorDto>>) {
                    if (response.isSuccessful) {
                        val groups = response.body()
                        enter_layout.removeView(account_layout)
                        enter_layout.addView(user_creator_layout)
                        val manager=LinearLayoutManager(requireContext())
//                        val converted= mutableListOf<GroupForCreatorDTO>()
//                        for (a in groups!!){
//                            converted.add(GroupForCreatorDTO(a.id,a.groupName,a.accessKey,a.groupMembers.size))
//                        }
                        creatoradapter.data= groups as MutableList<GroupCreatorForCreatorDto>
                        user_creator_groups_binding.recyclerAllGroup.layoutManager=manager
                        user_creator_groups_binding.recyclerAllGroup.adapter=creatoradapter


                    } else {
                        Toast.makeText(requireContext(), "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<GroupCreatorForCreatorDto>>, t: Throwable) {
                    // Обработка ошибок сети или других ошибок
                    Log.e("RetrofitError", "Ошибка: ${t.message}", t)
                    Toast.makeText(requireContext(), "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        //==================================================//


        val register_layout = register_layout_binding.RegistrationLayout

        registerbutton.setOnClickListener()
        {
            val enter_layout_embedded = enter_binding.enterLayoutEmbedded

            enter_layout.removeView(enter_layout_embedded)

            enter_layout.addView(register_layout)
        }

        val registr_name = register_layout_binding.editTextName

        val registr_login = register_layout_binding.editTextEmail

        val registr_pass = register_layout_binding.editTextPassword

        var registrname = registr_name.text

        var registrlogin = registr_login.text

        var registrpass = registr_pass.text

        val registration_button = register_layout_binding.buttonRegister

        registration_button.setOnClickListener()
        {
            try {
                val call_register = apiService.register(
                    registrlogin.toString(),
                    registrpass.toString(),
                    registrname.toString()
                )
                call_register.enqueue(object : Callback<Boolean> {
                    @OptIn(UnstableApi::class)
                    @SuppressLint("SuspiciousIndentation")
                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        if (response.isSuccessful) {
                            val registrationState = response.body()
                            if (registrationState == false) {
                                Toast.makeText(
                                    requireContext(),
                                    "Такой логин уже существует",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Успешная регистрация",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            val registration_layout = register_layout_binding.RegistrationLayout
                            val enter_layout_embedded = enter_binding.enterLayoutEmbedded
                            enter_layout.removeView(registration_layout)

                            enter_layout.addView(enter_layout_embedded)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Внутренняя ошибка: ${response.errorBody()?.string()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    @OptIn(UnstableApi::class)
                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        // Обработка ошибок сети или других ошибок
                        Log.e("RetrofitError", "Ошибка: ${t.message}", t)
                        Toast.makeText(context, "Ошибка сети: ${t.message}", Toast.LENGTH_LONG)
                            .show()
                    }
                })
            }catch (e:Exception) {
                Log.e("RetrofitError", "Внутренняя: ${e.message}", e)
                Toast.makeText(
                    requireContext(),
                    "Внутренняя ошибка: ${{e.message}}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        account_exit_button.setOnClickListener()
        {
            val enter_layout_embedded = enter_binding.enterLayoutEmbedded

            login_edit.text.clear()

            password_edit.text.clear()



            MainActivity.DataManager.setUserData(null)
            val sharedPreferences = requireContext().getSharedPreferences("data", Context.MODE_PRIVATE)
            sharedPreferences.edit().clear()
                /*.remove("key_id")
                .remove("key_name")*/
                .apply()

            enter_layout.removeView(account_layout)

            enter_layout.addView(enter_layout_embedded)
        }

        enterbutton.setOnClickListener()
        {
            val call_login = apiService.login(login_edit.text.toString(), password_edit.text.toString())

            call_login.enqueue(object : Callback<UserDTO> {
                override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        user?.let {
                            when (user.id) {
                                (-1).toLong() -> {
                                    Toast.makeText(requireContext(),"Такого логина не существует", Toast.LENGTH_SHORT).show()
                                }
                                (-2).toLong() -> {
                                    Toast.makeText(requireContext(),"Неверный пароль", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    Toast.makeText(requireContext(), "Успешная авторизация", Toast.LENGTH_SHORT).show()
                                    val enter_layout_embedded = enter_binding.enterLayoutEmbedded
                                    MainActivity.DataManager.setUserData(user)
                                    val sharedPreferences = requireContext().getSharedPreferences("data", Context.MODE_PRIVATE)
                                    sharedPreferences.edit()
                                        .putLong("key_id",user.id)
                                        .putString("key_name",user.name)
                                        .apply()
                                    enter_layout.removeView(enter_layout_embedded)

                                    enter_layout.addView(account_layout)

                                    auth_flag = 1

                                }
                            }
                        }
                    } else {
                        // Обработка ошибок
                        Toast.makeText(requireContext(),"Внутренняя ошибка: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }
                @OptIn(UnstableApi::class) override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                    // Обработка ошибок сети или других ошибок
                    Log.e("RetrofitError", "Ошибка: ${t.message}", t)
                    Toast.makeText(context, "Ошибка сети: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }





        return enter_binding.enterLayout
    }
}