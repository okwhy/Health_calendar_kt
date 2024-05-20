package com.sus.calendar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.media3.common.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.sus.calendar.MainActivity
import com.sus.calendar.RetrofitClient
import com.sus.calendar.databinding.CardGroupBinding
import com.sus.calendar.dtos.GroupDTOforUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinedGroupsRecyclerViewAdapter:RecyclerView.Adapter<JoinedGroupsRecyclerViewAdapter.ViewHolder>() {
    var data:MutableList<GroupDTOforUser> = mutableListOf()
        set(newValue){
            field=newValue
            notifyDataSetChanged()
        }
    class ViewHolder(val binding:CardGroupBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =CardGroupBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int =data.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element=data[position]
        val context = holder.itemView.context
        with(holder.binding){
            val apiService = RetrofitClient.instance
            textMyGroup.text=element.groupName
            exitGroup.setOnClickListener{
                val id_user= MainActivity.DataManager.getUserData()!!.id
                val call_delete_user=apiService.delete_user(id_user,element.id)
                call_delete_user.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if(response.isSuccessful){
                            data.remove(element)
                            notifyDataSetChanged()
                        } else {
                            Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
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