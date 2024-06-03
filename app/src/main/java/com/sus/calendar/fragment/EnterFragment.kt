package com.sus.calendar.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sus.calendar.MainActivity
import com.sus.calendar.R
import com.sus.calendar.RetrofitClient
import com.sus.calendar.databinding.EnterBinding
import com.sus.calendar.dtos.UserDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EnterFragment: Fragment() {
    private lateinit var enterBinding: EnterBinding;
    private lateinit var alertDialog: AlertDialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {
        val apiService=RetrofitClient.instance
        enterBinding=EnterBinding.inflate(inflater,container,false)
        val root =enterBinding.root
        enterBinding.buttonRegister.setOnClickListener(){
            val calllogin = apiService.login(enterBinding.editTextEmail.text.toString(),enterBinding.editTextPassword.text.toString())
            calllogin.enqueue(object : Callback<UserDTO> {
                override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        user?.let {
                            when (user.id) {
                                (-1).toLong() -> {
                                    Toast.makeText(requireContext(),"Такого логина не существует", Toast.LENGTH_SHORT).show()
                                    enterBinding.editTextEmail.text.clear()
                                    enterBinding.editTextPassword.text.clear()
                                }
                                (-2).toLong() -> {
                                    Toast.makeText(requireContext(),"Неверный пароль", Toast.LENGTH_SHORT).show()
                                    enterBinding.editTextPassword.text.clear()
                                }
                                else -> {
                                    Toast.makeText(requireContext(), "Успешная авторизация", Toast.LENGTH_SHORT).show()

                                    MainActivity.DataManager.setUserData(user)
                                    val sharedPreferences = requireContext().getSharedPreferences("data", Context.MODE_PRIVATE)
                                    sharedPreferences.edit()
                                        .putLong("key_id",user.id)
                                        .putString("key_name",user.name)
                                        .apply()
                                    val action=EnterFragmentDirections.actionNavAccountToNavUserPage(user.id)
                                    enterBinding.editTextEmail.text.clear()
                                    enterBinding.editTextPassword.text.clear()
                                    findNavController().navigate(action)
                                }
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(),"Внутренняя ошибка: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                    Toast.makeText(context, "Ошибка сети: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
        enterBinding.regist.setOnClickListener{
            findNavController().navigate(R.id.action_enterFragment_to_registrationFragment)
        }
        return root
    }
    private fun showPopup() {
        val context = requireContext()
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.popup_layout, null)
        val popupText = popupView.findViewById<TextView>(R.id.popupText)
        val closeButton = popupView.findViewById<Button>(R.id.closeButton)
        popupText.text = getString(R.string.help_desc).trimIndent()
        closeButton.setOnClickListener { alertDialog.dismiss() }
        builder.setView(popupView)
        alertDialog = builder.create()
        alertDialog.show()
    }
}