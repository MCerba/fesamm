package com.dawson.fesamm;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class ViewNoteActivity extends AppCompatActivity {

    private NoteViewModel mNoteViewModel;
    private String noteStr;
    private int noteId;
    private EditText editView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        this.editView = (EditText) findViewById(R.id.editNote);

        if(getIntent().hasExtra("currentNote")) {
            this.noteStr = getIntent().getExtras().getString("currentNote", "nothing :(");
            this.noteId = getIntent().getExtras().getInt("currentId");
            this.editView.setText(this.noteStr);

        }
    }

    public void updateNote(View v) {
        String newNoteStr = this.editView.getText().toString();

        Note newNote = new Note(newNoteStr);
        newNote.setId(noteId);

        mNoteViewModel.update(newNote);

        finish();
    }

    public void deleteNote(View v) {
        Note delNote = new Note(this.noteStr);
        delNote.setId(this.noteId);
        mNoteViewModel.delete(delNote);

        finish();
    }

    public void closePage(View v) {
        finish();
    }

    @Override
    public void onBackPressed(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.backPressed).setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateNote(null);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
