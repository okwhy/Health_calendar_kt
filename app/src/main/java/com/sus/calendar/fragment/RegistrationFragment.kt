package com.sus.calendar.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.sus.calendar.RetrofitClient
import com.sus.calendar.databinding.RegistrationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationFragment:Fragment() {
    private lateinit var registrationBinding: RegistrationBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registrationBinding=RegistrationBinding.inflate(inflater,container,false)
        val apiService=RetrofitClient.instance
        registrationBinding.buttonRegister.setOnClickListener(){
            val callregister = apiService.register(
                registrationBinding.editTextEmail.text.toString(),
                registrationBinding.editTextPassword.text.toString(),
                registrationBinding.editTextName.text.toString()
            )
            callregister.enqueue(object : Callback<Boolean> {
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
                        findNavController().navigateUp()

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Внутренняя ошибка: ${response.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    // Обработка ошибок сети или других ошибок
                    Toast.makeText(context, "Ошибка сети: ${t.message}", Toast.LENGTH_LONG)
                        .show()
                }
            })
        }
        registrationBinding.backToMainFrom.setOnClickListener(){
            findNavController().navigateUp()
        }
        val root = registrationBinding.root
        return root
    }
}