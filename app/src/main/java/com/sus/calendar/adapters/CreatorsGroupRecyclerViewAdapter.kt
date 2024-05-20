package com.sus.calendar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sus.calendar.RetrofitClient
import com.sus.calendar.databinding.CarGroupForCreaterBinding
import com.sus.calendar.dtos.getgroupcreator.subdtos.GroupForCreatorDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreatorsGroupRecyclerViewAdapter:RecyclerView.Adapter<CreatorsGroupRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(val binding: CarGroupForCreaterBinding):RecyclerView.ViewHolder(binding.root)
    var data:MutableList<GroupForCreatorDTO> = mutableListOf()
        set(newValue){
            field=newValue
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val binding=CarGroupForCreaterBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val element=data[position]
        val context=holder.itemView.context
        with(holder.binding){
            val apiService=RetrofitClient.instance
            textMyGroup.text=element.groupName
            textCountPeopleInGroup.text= element.count.toString()
            exitGroup.setOnClickListener {
                val id=element.id
                val callDeleteGroup=apiService.delete_group(id)
                callDeleteGroup.enqueue(object :Callback<Void>{
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