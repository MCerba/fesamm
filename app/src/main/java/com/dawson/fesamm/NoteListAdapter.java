package com.dawson.fesamm;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * RecyclerViwe Adapter for the notes that will be displayed to the user
 * @author Sonya Rabhi
 */
public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder> {

    private Context context;

    /**
     * A ViewHolder that will contain each view displayed in the RecyclerView
     */
    class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView noteItemView;

        // Find the view that will contain each Note
        private NoteViewHolder(View itemView) {
            super(itemView);
            noteItemView = itemView.findViewById(R.id.textView);
            noteItemView.setTextSize(20);
        }
    }

    private final LayoutInflater mInflater;
    private List<Note> mNotes; // Cached copy of words

    NoteListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the recyclerview and return a new NoteViewHolder
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder holder, int position) {

        // If the note exists, add it to the recycler view
        if (mNotes != null) {
            Note current = mNotes.get(position);

            final String currentNote = current.getNote();
            final int currentId = current.getId();

            holder.noteItemView.setText(current.getNote());

            // Add an event listener for each Note clicked that opens a new Activity to manage the said note
            holder.noteItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewNoteActivity.class);
                    intent.putExtra("currentNote", currentNote);
                    intent.putExtra("currentId", currentId);
                    context.startActivity(intent);
                }
            });
        } else {
            // Covers the case of data not being ready yet.
            holder.noteItemView.setText("No Notes");
        }
    }

    /**
     * Add a new list of notes to the adapter and notify the changes
     * @param notes
     */
    void setNotes(List<Note> notes){
        mNotes = notes;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mNotes != null)
            return mNotes.size();
        else return 0;
    }
}
