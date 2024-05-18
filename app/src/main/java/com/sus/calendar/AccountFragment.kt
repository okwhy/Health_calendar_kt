package com.sus.calendar

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
import com.sus.calendar.databinding.AccountLayoutBinding
import com.sus.calendar.databinding.AccountPageBinding


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountFragment : Fragment() {
    private lateinit var enter_binding: EnterBinding
    private lateinit var account_layout_binding: AccountLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        enter_binding = EnterBinding.inflate(inflater,container,false)

        account_layout_binding = AccountLayoutBinding.inflate(inflater,container,false)

        val exit_layout = AccountLayoutBinding.inflate(inflater,container,false)
        val group_member_layout = AccountLayoutBinding.inflate(inflater,container,false)
        val group_creator_layout = AccountLayoutBinding.inflate(inflater,container,false)

        val exit_button = exit_layout.accountText
        val group_member_button = group_member_layout.accountText
        val group_creator_button = group_creator_layout.accountText
        exit_button.text = "Выйти"
        group_member_button.text = "Участие в группах"
        group_creator_button.text = "Создание групп"

        val login_edit = enter_binding.editTextEmail

        val password_edit = enter_binding.editTextPassword

        val enterbutton = enter_binding.buttonRegister

        val registerbutton = enter_binding.Regist


        var triedlogin = login_edit.text

        var triedpass = password_edit.text

        val apiService = RetrofitClient.instance

        enterbutton.setOnClickListener()
        {
            val call = apiService.login(triedlogin.toString(), triedpass.toString())

            call.enqueue(object : Callback<Long> {
                override fun onResponse(call: Call<Long>, response: Response<Long>) {
                    if (response.isSuccessful) {
                        val user_id = response.body()
                        user_id?.let {
                            if(user_id?.toInt() == -1)
                            {
                                Toast.makeText(requireContext(),"Такого логина не существует", Toast.LENGTH_SHORT).show()
                            }
                            else if (user_id?.toInt() == -2)
                            {
                                Toast.makeText(requireContext(),"Неверный пароль", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                Toast.makeText(requireContext(), "Успешная авторизация", Toast.LENGTH_SHORT).show()
                                val enter_layout = enter_binding.enterLayout

                                enter_layout.removeView(login_edit)
                                enter_layout.removeView(password_edit)
                                enter_layout.removeView(enterbutton)
                                enter_layout.removeView(registerbutton)

                                enter_layout.addView(exit_layout.accountLayout)
                                enter_layout.addView(group_member_layout.accountLayout)
                                enter_layout.addView(group_creator_layout.accountLayout)

                            }
                        }
                    } else {
                        // Обработка ошибок
                        Toast.makeText(requireContext(),"Внутренняя ошибка: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }
                @OptIn(UnstableApi::class) override fun onFailure(call: Call<Long>, t: Throwable) {
                    // Обработка ошибок сети или других ошибок
                    Log.e("RetrofitError", "Ошибка: ${t.message}", t)
                    Toast.makeText(context, "Ошибка сети: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }

        return enter_binding.root
    }
}