package com.sus.calendar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sus.calendar.R
import com.sus.calendar.RetrofitClient
import com.sus.calendar.databinding.CarGroupForCreaterBinding
import com.sus.calendar.databinding.CreateGroupBinding
import com.sus.calendar.databinding.EnterBinding
import com.sus.calendar.databinding.GroupCardForCreatorBinding
import com.sus.calendar.databinding.MyGroupsBinding
import com.sus.calendar.dtos.UserDTO
import com.sus.calendar.dtos.getgroupcreator.GroupCreatorForCreatorDto
import com.sus.calendar.dtos.getgroupcreator.subdtos.GroupForCreatorDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreatorsGroupRecyclerViewAdapter(
    private val enter_binding: EnterBinding,
    private val groupsBinding: CreateGroupBinding,
    private val userLayoutBinding:GroupCardForCreatorBinding

) :
    RecyclerView.Adapter<CreatorsGroupRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(val binding: CarGroupForCreaterBinding) : RecyclerView.ViewHolder(binding.root)

    var data: MutableList<GroupCreatorForCreatorDto> = mutableListOf()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }
    private val dataUsers:MutableList<UserDTO> = mutableListOf()
    fun setUsers(users:MutableList<UserDTO>){
        dataUsers.addAll(users)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CarGroupForCreaterBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = data[position]
        val context = holder.itemView.context
        with(holder.binding) {
            val apiService = RetrofitClient.instance
            textMyGroup.text = element.groupName
            textCountPeopleInGroup.text = element.groupMembers.size.toString()
            exitGroup.setOnClickListener {
                val id = element.id
                val callDeleteGroup = apiService.delete_group(id)
                callDeleteGroup.enqueue(object : Callback<Void> {
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
            textMyGroup.setOnClickListener {
                enter_binding.enterLayout.removeView(groupsBinding.GroupCreatorLayout)
                enter_binding.enterLayout.addView(userLayoutBinding.UserGroupLayout)
                userLayoutBinding.textMygroup.text= element.groupName
                userLayoutBinding.passwordOfGroup.setText(element.accessKey)
                val manager = LinearLayoutManager(context)
                val adapter=GroupMembersRecyclerViewAdapter(element.id)
                adapter.data= element.groupMembers.map { x->x.fkUser }.toMutableList()
                userLayoutBinding.recyclerMygroup.layoutManager=manager
                userLayoutBinding.recyclerMygroup.adapter=adapter
//                val tmp = root.findViewById<ConstraintLayout>(R.id.enter_layout)|
//                tmp.removeView(root.findViewById<LinearLayoutCompat>(R.id.GroupCreatorLayout))|
//                tmp.addView(root.findViewById<LinearLayoutCompat>(R.id.UserGroupLayout))|
//                val view = tmp.findViewById<LinearLayoutCompat>(R.id.UserGroupLayout)|
//                view.findViewById<TextView>(R.id.textMygroup).text = element.groupNam
//                view.findViewById<EditText>(R.id.passwordOfGroup).setText(element.accessKey|
//                val manager = LinearLayoutManager(context)
//                val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerMygroup)
//                recyclerView.adapter = GroupMembersRecyclerViewAdapter(element.id)
//                recyclerView.layoutManager = manager

            }

        }
    }
}