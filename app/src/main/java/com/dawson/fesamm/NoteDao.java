package com.dawson.fesamm;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * A Data Access Object that communicates with the NoteRoomRepository to retrieve the necessary
 * information from the database
 * @author Sonya Rabhi
 */
@Dao
public interface NoteDao {
    @Insert
    void insert(Note note);

    @Query("DELETE FROM note_table")
    void deleteAll();

    @Query("DELETE FROM note_table WHERE id = :id")
    void deleteNote (int id);

    @Query("SELECT * FROM note_table")
    LiveData<List<Note>> getAllNotes();

    @Query("UPDATE note_table SET note = :note WHERE id = :id")
    void update(String note, int id);
}
