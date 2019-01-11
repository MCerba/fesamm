package com.dawson.fesamm;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * An object that represents a note with all of its necessary getters and setters
 * @author Sonya Rabhi
 */
@Entity(tableName = "note_table")
public class Note {
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "note")
    private String mNote;

    public Note(String note) {
        this.mNote = note;
    }

    public void setId(int id) { this.id = id; }

    public int getId() {return this.id;}

    public String getNote(){return this.mNote;}

}
