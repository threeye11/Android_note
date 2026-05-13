package com.example.mynote;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditNoteActivity extends AppCompatActivity {

    private EditText etTitle, etContent;
    private Button btnSave;
    private NoteRepository repository;
    private Note existingNote = null;
    private int noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        btnSave = findViewById(R.id.btn_save);
        Button btnDelete = findViewById(R.id.btn_delete);
        repository = new NoteRepository(this);

        // 获取传入的笔记ID
        noteId = getIntent().getIntExtra("note_id", -1);
        if (noteId != -1) {
            existingNote = repository.getNoteById(noteId);
            if (existingNote != null) {
                etTitle.setText(existingNote.getTitle());
                etContent.setText(existingNote.getContent());
            }
        }
        btnDelete.setOnClickListener(v -> {
            if (existingNote != null) {
                repository.deleteNote(existingNote.getId());
                Toast.makeText(this, "笔记已删除", Toast.LENGTH_SHORT).show();
                finish(); // 关闭当前页面，返回主界面
            } else {
                // 如果是新建笔记尚未保存，直接返回
                finish();
            }
        });

        btnSave.setOnClickListener(v -> saveNote());
    }

    private void saveNote() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "标题不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        if (existingNote == null) {
            // 新增笔记
            Note newNote = new Note(title, content, currentTime, currentTime);
            long id = repository.insertNote(newNote);
            if (id != -1) {
                Toast.makeText(this, "笔记已保存", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            // 更新笔记
            existingNote.setTitle(title);
            existingNote.setContent(content);
            existingNote.setUpdatedTime(currentTime);
            int rows = repository.updateNote(existingNote);
            if (rows > 0) {
                Toast.makeText(this, "笔记已更新", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}