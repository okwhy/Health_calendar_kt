package com.sus.calendar

import ApiService
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sus.calendar.databinding.AccountPageBinding
import com.sus.calendar.databinding.ActivityMainBinding
import com.sus.calendar.databinding.EnterBinding
import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountFragment : Fragment() {
    private lateinit var binding: EnterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EnterBinding.inflate(inflater,container,false)

        val login_edit = binding.editTextEmail

        val password_edit = binding.editTextPassword

        val enterbutton = binding.buttonRegister

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
                        // Обработка данных пользователя
                        user_id?.let {
                            // Использование данных пользователя
                            Toast.makeText(requireContext(),"User ID: ${it}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Обработка ошибок
                        Toast.makeText(requireContext(),"Error: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }
                @OptIn(UnstableApi::class) override fun onFailure(call: Call<Long>, t: Throwable) {
                    // Обработка ошибок сети или других ошибок
                    Log.e("RetrofitError", "Ошибка: ${t.message}", t)
                    Toast.makeText(context, "Ошибка: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }

        return binding.root
    }
}