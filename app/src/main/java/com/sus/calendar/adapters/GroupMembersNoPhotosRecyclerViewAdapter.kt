package com.sus.calendar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

import com.sus.calendar.RetrofitClient

import com.sus.calendar.databinding.CardUserBinding
import com.sus.calendar.dtos.getgroupcreator.UserInGroupDto
import com.sus.calendar.fragment.UsersOfGroupDirections
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GroupMembersNoPhotosRecyclerViewAdapter  (val idUser:Long) :
    RecyclerView.Adapter<GroupMembersNoPhotosRecyclerViewAdapter.ViewHolder>() {
    var data: MutableList<UserInGroupDto> = mutableListOf()
        get() = field
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    class ViewHolder(val binding: CardUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardUserBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = data[position]
        val context = holder.itemView.context

        with(holder.binding) {
            val apiService = RetrofitClient.instance
            usernametext.text = element.name
            usernametext.setOnClickListener {
                val action=UsersOfGroupDirections.actionUsersOfGroupToStatisticMember(element)
                it.findNavController().navigate(action)
            }
            kickUser.setOnClickListener {
                val call_delete_user = apiService.delete_user(element.id, idUser)
                call_delete_user.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            data.remove(element)
                            notifyDataSetChanged()
                        } else {
                            Toast.makeText(
                                context,
                                "Error: ${response.message()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(context, "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }
    }


}

   