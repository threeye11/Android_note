package com.example.mynote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private NoteRepository repository;
    private List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = new NoteRepository(this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
            startActivity(intent);
        });

        loadNotes();
    }

    private void loadNotes() {
        noteList = repository.getAllNotes();
        if (noteAdapter == null) {
            noteAdapter = new NoteAdapter(noteList, note -> {
                // 点击编辑
                Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                intent.putExtra("note_id", note.getId());
                startActivity(intent);
            }, note -> {
                // 长按删除
                repository.deleteNote(note.getId());
                loadNotes();
                Toast.makeText(this, "笔记已删除", Toast.LENGTH_SHORT).show();
            });
            recyclerView.setAdapter(noteAdapter);
        } else {
            noteAdapter.updateNotes(noteList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes(); // 每次返回时刷新列表
    }

    // RecyclerView Adapter 内部类
    static class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
        private List<Note> notes;
        private final OnItemClickListener clickListener;
        private final OnItemLongClickListener longClickListener;

        public interface OnItemClickListener {
            void onItemClick(Note note);
        }

        public interface OnItemLongClickListener {
            void onItemLongClick(Note note);
        }

        public NoteAdapter(List<Note> notes, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
            this.notes = notes;
            this.clickListener = clickListener;
            this.longClickListener = longClickListener;
        }

        public void updateNotes(List<Note> newNotes) {
            this.notes = newNotes;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            android.view.View view = android.view.LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_note, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Note note = notes.get(position);
            holder.title.setText(note.getTitle());
            holder.content.setText(note.getContent());
            holder.time.setText(note.getUpdatedTime());
            holder.itemView.setOnClickListener(v -> clickListener.onItemClick(note));
            holder.itemView.setOnLongClickListener(v -> {
                longClickListener.onItemLongClick(note);
                return true;
            });
        }

        @Override
        public int getItemCount() {
            return notes.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            android.widget.TextView title, content, time;
            public ViewHolder(android.view.View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.item_title);
                content = itemView.findViewById(R.id.item_content);
                time = itemView.findViewById(R.id.item_time);
            }
        }
    }
}