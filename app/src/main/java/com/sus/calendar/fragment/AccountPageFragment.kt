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
import androidx.media3.common.util.Log
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.sus.calendar.MainActivity
import com.sus.calendar.R
import com.sus.calendar.RetrofitClient
import com.sus.calendar.databinding.AccountPageBinding
import com.sus.calendar.dtos.getgroupcreator.GroupCreatorForCreatorDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountPageFragment: Fragment() {
    private lateinit var alertDialog: AlertDialog
    private lateinit var accountPageBinding: AccountPageBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        accountPageBinding=AccountPageBinding.inflate(inflater,container,false)
        val root=accountPageBinding.root
        val apiService=RetrofitClient.instance
        val args:AccountPageFragmentArgs by navArgs()
        accountPageBinding.Registration.setOnClickListener{
            MainActivity.DataManager.setUserData(null)
            val sharedPreferences = requireContext().getSharedPreferences("data", Context.MODE_PRIVATE)
            sharedPreferences.edit().clear()
                /*.remove("key_id")
                .remove("key_name")*/
                .apply()
            findNavController().navigateUp()
        }
        accountPageBinding.Help.setOnClickListener{
            showPopup()
        }
        accountPageBinding.MyGroup.setOnClickListener{
            findNavController().navigate(R.id.action_nav_user_page_to_nav_joined)
        }
        accountPageBinding.MyGroups.setOnClickListener{

            val call_creator_groups = apiService.get_creator_groups(args.id)

            call_creator_groups.enqueue(object : Callback<List<GroupCreatorForCreatorDto>> {
                override fun onResponse(call: Call<List<GroupCreatorForCreatorDto>>, response: Response<List<GroupCreatorForCreatorDto>>) {
                    if (response.isSuccessful) {
                        val groups = response.body()
                        val action= groups?.let { it1 ->
                            AccountPageFragmentDirections.actionNavUserPageToNavCreatedGroups(
                                it1.toTypedArray())
                        }
                        if (action != null) {
                            findNavController().navigate(action)
                        }

                    } else {
                        Toast.makeText(requireContext(), "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<GroupCreatorForCreatorDto>>, t: Throwable) {
                    Toast.makeText(requireContext(), "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
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
        closeButton.setOnClickListener { alertDialog!!.dismiss() }
        builder.setView(popupView)
        alertDialog = builder.create()
        alertDialog.show()
    }
}