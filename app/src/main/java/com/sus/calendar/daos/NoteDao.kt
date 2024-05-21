package com.sus.calendar.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sus.calendar.entities.Note

@Dao
interface NoteDao {
    @get:Query("select * from note")
    val all: List<Note?>?

    @Update
    fun update(note: Note)

    @Insert
    fun insert(note: Note): Long

    @Delete
    fun delete(note: Note)

    @Query("select * from note where note.type = :type")
    fun getByCat(type: String): List<Note?>?

    @Query("select * from note where note.type = :type and note.value=:value")
    fun getByCat(type: String, value: Double): List<Note?>?

    @Query("select * from note where note.type = :type and note.id_fkdate=:id")
    fun getByCatAndFKId(type: String?, id: Long): Note?
}
