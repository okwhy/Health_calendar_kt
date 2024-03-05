package com.sus.calendar.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
class Note : Serializable {
    constructor(type: String?, value: String?, id_fkdate: Long) {
        this.type = type
        this.value = value
        this.id_fkdate = id_fkdate
    }

    constructor(id: Long, type: String?, value: String?, id_fkdate: Long) {
        this.id = id
        this.type = type
        this.value = value
        this.id_fkdate = id_fkdate
    }

    override fun toString(): String {
        return "Note{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", id_fkdate=" + id_fkdate +
                '}'
    }

    constructor()

    @JvmField
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    @JvmField
    var type: String? = null
    @JvmField
    var value: String? = null
    @JvmField
    var id_fkdate: Long = 0
}
